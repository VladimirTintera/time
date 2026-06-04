#!/bin/zsh

# Ukončí skript, pokud jakýkoli příkaz selže
set -e

# Kontrola, zda uživatel zadal text commitu
if [ -z "$1" ]; then
    echo "❌ Chyba: Musíš zadat zprávu pro commit!"
    echo "Použití: ./scripts/prerelease.sh \"Zpráva commitu v angličtině\""
    exit 1
fi

COMMIT_MSG="$1"
CURRENT_BRANCH=$(git branch --show-current)

echo "🚦 Spouštím prerelease fázi z větve: '$CURRENT_BRANCH'"

# 1. Kontrola, zda je pracovní adresář čistý
if ! git diff-index --quiet HEAD --; then
    echo "❌ Chyba: V repozitáři jsou neodevzdané změny. Commitni nebo stasni je před spuštěním release."
    exit 1
fi

# 2. Spuštění testů
echo "🧪 Spouštím unit testy..."
./gradlew check

# 3. Generování Dokka dokumentace
echo "📚 Generuji Dokka HTML dokumentaci..."
./gradlew dokkaGenerateHtml

# 4. Sestavení JS webové aplikace a zkopírování do docs/demo
echo "💻 Sestavuji JS webovou aplikaci a kopíruji do docs/demo..."
./gradlew :webApp:copyJsDistToDocs

# 5. Commitování změn v dokumentaci a dema ve vývojovém repozitáři
echo "📝 Kontroluji změny v generovaných docs a demo..."
git add docs/
if ! git diff-index --cached --quiet HEAD --; then
    echo "💾 Ukládám vygenerovanou dokumentaci a demo do vývojového repozitáře..."
    git commit -m "docs: update generated API docs and web demo for release"
else
    echo "✅ Dokumentace a demo jsou beze změn."
fi

# 6. Ověření a napojení public repozitáře
echo "🔍 Ověřuji napojení public repozitáře..."
PUBLIC_URL=$(git remote get-url public 2>/dev/null || true)
EXPECTED_URL="https://github.com/VladimirTintera/time.git"

if [ -z "$PUBLIC_URL" ]; then
    echo "➕ Přidávám chybějící remote 'public'..."
    git remote add public "$EXPECTED_URL"
else
    echo "✅ Remote 'public' je správně napojen na: $PUBLIC_URL"
fi

# 7. Příprava čistého zrcadlení
echo "🔄 Synchronizuji a stahuji nejnovější stav z public..."
git fetch public

echo "🌿 Vytvářím synchronizační větev z public/main..."
# Odstraní lokální sync-release větev, pokud by náhodou existovala z minulého běhu
git branch -D sync-release 2>/dev/null || true
git checkout -b sync-release public/main

echo "🧹 Odstraňuji zastaralé soubory (zrcadlení smazaných souborů)..."
# Odstraníme všechny sledované soubory z public větve v pracovním adresáři
git rm -rf . > /dev/null

echo "📥 Kopíruji aktuální stav z vývojové větve '$CURRENT_BRANCH'..."
# Vytáhneme všechny soubory z vývojové větve
git checkout "$CURRENT_BRANCH" -- .

# Znovu přidáme všechny soubory k zapsání
git add -A

# Zkontrolujeme, zda vůbec existují nějaké změny oproti public/main
if git diff-index --cached --quiet HEAD --; then
    echo "ℹ️ Žádné změny k synchronizaci s public repozitářem (public je již shodný s dev)."
    git checkout "$CURRENT_BRANCH"
    git branch -D sync-release
else
    echo "✍️ Vytvářím synchronizační commit..."
    git commit -m "$COMMIT_MSG"

    echo "🚀 Odesílám změny do public repozitáře..."
    git push public sync-release:main

    # Návrat na původní větev a úklid
    git checkout "$CURRENT_BRANCH"
    git branch -D sync-release
fi

echo ""
echo "========================================================================="
echo "🎉 PRERELEASE FÁZE BYLA ÚSPĚŠNĚ DOKONČENA!"
echo "========================================================================="
echo "Kód a vygenerovaná dokumentace byly úspěšně zrcadleny do public repozitáře."
echo ""
echo "Pro dokončení celého RELEASE procesu můžeš provést následující kroky:"
echo "1. Ověř, že web demo funguje a dokumentace se zobrazuje správně:"
echo "   - Demo: https://vladimirtintera.github.io/time/demo/"
echo "   - Dokumentace: https://vladimirtintera.github.io/time/"
echo ""
echo "2. Vytvoř a odešli git tag přímo z tohoto adresáře:"
echo "   git fetch public"
echo "   git tag -a v1.0.0 public/main -m \"Release v1.0.0\""
echo "   git push public v1.0.0"
echo "   (Nahraď 'v1.0.0' požadovanou verzí)"
echo ""
echo "3. Vytvoř oficiální GitHub Release pro stažení a zobrazení v seznamu releasů:"
echo "   Otevři URL: https://github.com/VladimirTintera/time/releases/new?tag=v1.0.0"
echo "========================================================================="
echo ""
