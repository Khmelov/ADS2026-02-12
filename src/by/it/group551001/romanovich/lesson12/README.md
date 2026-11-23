# Лабораторная 12

`MyAvlMap` хранит пары Integer/String в AVL-дереве с высотами и поворотами.
`MyRbMap` использует левостороннее красно-чёрное дерево (LLRB).
`MySplayMap` поднимает найденный или последний посещённый узел к корню поворотами zig, zig-zig и zig-zag.
Готовые коллекции для хранения дерева не используются.

Реализованы обязательные методы уровней A/B/C. Ключи не могут быть null, значения могут.
Диапазоны `headMap`, `tailMap`, `subMap` возвращают самостоятельные карты.
Необязательные представления `keySet`, `values`, `entrySet` и обратные представления
не реализованы и выбрасывают `UnsupportedOperationException`.

Из корня репозитория с JDK 25:

```bash
mkdir -p /tmp/romanovich-labs
javac -encoding UTF-8 -cp init.jar -d /tmp/romanovich-labs src/by/it/group551001/romanovich/lesson12/*.java
java -cp /tmp/romanovich-labs:init.jar org.junit.runner.JUnitCore by.it.group551001.romanovich.lesson12.Test_Part2_Lesson12Test by.it.group551001.romanovich.lesson12.Lesson12RegressionTest
```

Дополнительные тесты сравнивают карты с TreeMap, проверяют BST-порядок, высоты AVL,
цвета и чёрную высоту LLRB, родительские ссылки и поведение splay на длинной цепочке.
