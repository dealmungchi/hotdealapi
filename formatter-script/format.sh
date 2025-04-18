find /Users/jun/dev/sidep/hotdealapi/src -name "*.java" -type f | while read file; do sed -i "" "s/^    /  /g" "$file"; done
