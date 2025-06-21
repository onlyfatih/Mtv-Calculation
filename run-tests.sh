#!/bin/bash

MODE=$1  # chrome, firefox, all

CHROME_SUITE="testng-chrome.xml"
FIREFOX_SUITE="testng-firefox.xml"
CHROME_RESULTS="target/allure-results/chrome"
FIREFOX_RESULTS="target/allure-results/firefox"
FINAL_REPORT="target/allure-report"

echo "🧹 Cleaning previous results..."
rm -rf "$CHROME_RESULTS"
rm -rf "$FIREFOX_RESULTS"
rm -rf "$FINAL_REPORT"

mkdir -p "$CHROME_RESULTS"
mkdir -p "$FIREFOX_RESULTS"

run_tests() {
  local suite=$1
  local browser=$2
  local result_dir=$3

  echo " Running tests for $browser..."
  mvn test -DsuiteXmlFile="$suite" \
           -Dbrowser="$browser" \
           -Dallure.results.directory="$result_dir" \
           -DallureResultsDir="$result_dir"
}

if [[ "$MODE" == "chrome" || "$MODE" == "all" || -z "$MODE" ]]; then
  if [ ! -f "$CHROME_SUITE" ]; then
    echo " Chrome TestNG suite file not found: $CHROME_SUITE"
    exit 1
  fi
  run_tests "$CHROME_SUITE" "chrome" "$CHROME_RESULTS"
fi

if [[ "$MODE" == "firefox" || "$MODE" == "all" ]]; then
  if [ ! -f "$FIREFOX_SUITE" ]; then
    echo " Firefox TestNG suite file not found: $FIREFOX_SUITE"
    exit 1
  fi
  run_tests "$FIREFOX_SUITE" "firefox" "$FIREFOX_RESULTS"
fi

echo "Generating Allure report..."
if [ "$MODE" == "chrome" ]; then
  allure generate target/allure-results/chrome -o target/allure-report --clean
elif [ "$MODE" == "firefox" ]; then
  allure generate target/allure-results/firefox -o target/allure-report --clean
else
  allure generate \
    target/allure-results/chrome \
    target/allure-results/firefox \
    -o target/allure-report --clean
fi

echo " Opening Allure report..."
allure open "$FINAL_REPORT"
