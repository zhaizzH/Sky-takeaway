import sys

filepath = 'shopTable.fe534d8f.js'
with open(filepath, 'r', encoding='utf-8') as f:
    content = f.read()

old = '[1,3,4,5].includes(n.status)'
new = '[1,3,4].includes(n.status)'

count = content.count(old)
print(f'Found {count} occurrences')

if count > 0:
    content = content.replace(old, new)
    with open(filepath, 'w', encoding='utf-8') as f:
        f.write(content)
    print('Replacement done!')
else:
    print('Pattern not found')
