package by.it.group551004.pedko.lesson02;

import java.util.ArrayList;
import java.util.List;
/*
Даны события events
реализуйте метод calcStartTimes, так, чтобы число включений регистратора на
заданный период времени (1) было минимальным, а все события events
были зарегистрированы.
Алгоритм жадный. Для реализации обдумайте надежный шаг.
*/

public class A_VideoRegistrator {

    public static void main(String[] args) {
        A_VideoRegistrator instance = new A_VideoRegistrator();
        double[] events = new double[]{1, 1.1, 1.6, 2.2, 2.4, 2.7, 3.9, 8.1, 9.1, 5.5, 3.7};
        List<Double> starts = instance.calcStartTimes(events, 1); //рассчитаем моменты старта, с длинной сеанса 1
        System.out.println(starts);                            //покажем моменты старта
    }



    public static double[] merge(double array1[], double array2[]) {
        int i;
        int j;
        int index;
        int size1;
        int size2;
        int answerSize;
        double[] answerArray;

        i = 0;
        j = 0;
        index = 0;
        size1 = 0;
        size2 = 0;
        answerSize = 0;

        size1 = array1.length;
        size2 = array2.length;

        answerSize = size1 + size2;
        answerArray = new double[answerSize];

        while (i < size1 && j < size2) {
            if (array1[i] <= array2[j]) {
                answerArray[index] = array1[i];
                i++;
            } else {
                answerArray[index] = array2[j];
                j++;
            }
            index++;
        }

        while (i < size1) {
            answerArray[index] = array1[i];
            i++;
            index++;
        }

        while (j < size2) {
            answerArray[index] = array2[j];
            j++;
            index++;
        }

        return answerArray;
    }

    public static double[] copyArray(double[] source, double[] dest, int size) {
        int i;
        i = 0;

        dest = new double[size];

        for (i = 0; i < size; i++) {
            dest[i] = source[i];
        }
        return dest;
    }

    public static double[] copyArrayPart(double source[], int startIndex, int count, double[] dest) {
        int i;

        i = 0;

        dest = new double[count];

        for (i = 0; i < count; i++) {
            dest[i] = source[startIndex + i];
        }
        return dest;
    }

    public static double[] sortArray(double[] givenArray) {
        int i;
        int j;
        int k;
        int l;
        int arrayLength;
        int amountOfMerges;
        int size1;
        int size2;
        int size3;
        int arraySize;
        int arraySizeM1;
        double[] array1;
        double[] array2;
        double[] array3;
        int[] arrayOfLabelIndices;
        double[] newArray;
        double[] mergedArray;

        i = 0;
        j = 0;
        k = 0;
        arrayLength = 0;
        amountOfMerges = 0;
        size1 = 0;
        size2 = 0;
        size3 = 0;
        arraySize = givenArray.length;
        arraySizeM1 = arraySize - 1;
        array1 = null;
        array2 = null;
        array3 = null;
        arrayOfLabelIndices = null;
        newArray = null;
        mergedArray = null;

        newArray = copyArray(givenArray, newArray, arraySize);

        arrayLength = 1;

        while (arrayLength > 0) {
            i = 0;
            j = 0;
            k = 0;
            l = 0;
            arrayLength = 0;

            while (i < arraySizeM1) {
                if (givenArray[i] > givenArray[i + 1]) {
                    arrayLength++;
                }
                i++;
            }

            if (arrayLength > 0) {
                arrayOfLabelIndices = new int[arrayLength];
                i = 0;

                while (i < arraySizeM1) {
                    if (givenArray[i] > givenArray[i + 1]) {
                        arrayOfLabelIndices[j] = i;
                        j++;
                    }
                    i++;
                }

                amountOfMerges = (arrayLength + 1) / 2;

                for (i = 0; i < amountOfMerges; i++) {

                    if (i == 0) {
                        size1 = arrayOfLabelIndices[i] + 1;
                        array1 = copyArrayPart(givenArray, 0, size1, array1);
                        size2 = arraySize - arrayOfLabelIndices[arrayLength - 1] - 1;
                        array2 = copyArrayPart(givenArray, arrayOfLabelIndices[arrayLength - 1] + 1, size2, array2);
                    } else {
                        size1 = arrayOfLabelIndices[i] - arrayOfLabelIndices[i - 1];
                        array1 = copyArrayPart(givenArray, arrayOfLabelIndices[i - 1] + 1, size1, array1);
                        size2 = arrayOfLabelIndices[arrayLength - i] - arrayOfLabelIndices[arrayLength - i - 1];
                        array2 = copyArrayPart(givenArray, arrayOfLabelIndices[arrayLength - i - 1] + 1, size2, array2);
                    }

                    if ((arrayLength % 2 == 0) && (i == 0)) {
                        size3 = arrayOfLabelIndices[arrayLength / 2] - arrayOfLabelIndices[arrayLength / 2 - 1];
                        array3 = copyArrayPart(givenArray, arrayOfLabelIndices[arrayLength / 2 - 1] + 1, size3, array3);
                    }

                    mergedArray = merge(array1, array2);

                    for (l = 0; l < mergedArray.length; l++) {
                        newArray[k] = mergedArray[l];
                        k++;
                    }

                    if ((i == (amountOfMerges - 1)) && (arrayLength % 2 == 0)) {
                        for (l = 0; l < size3; l++) {
                            newArray[k] = array3[l];
                            k++;
                        }
                    }

                    array1 = null;
                    array2 = null;
                    mergedArray = null;
                }

                if (arrayLength % 2 == 0) {
                    array3 = null;
                }

                arrayOfLabelIndices = null;

                givenArray = newArray;
                newArray = null;
                newArray = copyArray(givenArray, newArray, arraySize);
            }
        }

        return givenArray;
    }




    //модификаторы доступа опущены для возможности тестирования
    List<Double> calcStartTimes(double[] events, double workDuration) {
        //events - события которые нужно зарегистрировать
        //timeWorkDuration время работы видеокамеры после старта
        List<Double> result;
        result = new ArrayList<>();
        int i = 0;                              //i - это индекс события events[i]
        //Комментарии от проверочного решения сохранены для подсказки, но вы можете их удалить.
        //Подготовка к жадному поглощению массива событий
        //hint: сортировка Arrays.sort обеспечит скорость алгоритма
        //C*(n log n) + C1*n = O(n log n)

        //пока есть незарегистрированные события
        //получим одно событие по левому краю
        //и запомним время старта видеокамеры
        //вычислим момент окончания работы видеокамеры
        //и теперь пропустим все покрываемые события
        //за время до конца работы, увеличивая индекс

        double lastStart;

        events = sortArray(events);

        lastStart = events[0];
        result.add(lastStart);

        for (i = 1; i < events.length; ++i) {
            if (lastStart + workDuration < events[i]) {
                lastStart = events[i];
                result.add(lastStart);
            }
        }

        return result;                        //вернем итог
    }
}
