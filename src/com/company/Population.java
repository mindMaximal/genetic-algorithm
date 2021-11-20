package com.company;
import java.util.*;

public class Population {
    int individualsCount;  // Счетчик особей
    int signsCount; // Счетчик признаков

    List<Individidual> population = new ArrayList<>(); // Множество особей в популяции

    private double[] func; // Заданная функция
    private double[] x; // x функции

    public Population() {
    }

    public Population(int individualsCount, int signsCount) {
        // Расчитываем заданную функцию
        this.x = Function.getArray(0.1, 0.01, 100); // x[0, 10]
        this.func = Function.getFunc(x);

        this.individualsCount = individualsCount;
        this.signsCount = signsCount;
    }

    /**
     * Функция старта алгоритма. В цикле: размножение, мутация, удаление особей.
     * Остановка цикла в случае, если эвклидово рассояние <= 0.5
     */
    public void start() {
        // Здесь в цикле будем размножать, уничтожать особи до тех пор, пока эвклидово расстояние не будет равно 0,5.
        final double BREAKPOINT = 0.5; // Точка останова

        double minDistance; // Минимальное эвклидово расстояние особей
        Individidual individualMinDistance; // особь с минимальным эвклидовым расстоянием

        int count = 0; // Кол-во популяций в цикле

        do {
            crossoverByThreePointsOperator();

            mutate();
            clear(); // Удаляем половину удвоенной популяции с минимальным эвклидовым расстоянием

            System.out.println("-- Популяция после чистки:");

            for (int i = 0; i < population.size(); i++){
                Individidual individidual = population.get(i);
                System.out.println((i + 1) + individidual.toString() + " euclidean distance: " + individidual.getEuclideanDistance());
            }

            // Ищем минимальное эвклидово расстояние
            individualMinDistance = getMinEuclideanDistance();
            minDistance = individualMinDistance.getEuclideanDistance();

            minDistance = Function.round(minDistance, 1); // Округлим для вывода

            System.out.println("Минимальное эвклидово растояние: " + minDistance);

            count++;

        } while (minDistance > BREAKPOINT);

        System.out.println("Кол-во поколений в цикле " + count);
    }

    /**
     * Начальная популяция: создаем 20 особей и заполняем признаки особей случайными числами
     */
    public void create(){
        // Создаем 20 особей, у каждой 100 признаков
        for (int i = 0; i < individualsCount; i++){
            double[] signs = Function.getRandomArray(signsCount, 0, 3); // Массив признаков [0, 3] для графика корня
            Individidual individidual = new Individidual(signsCount); // Особь

            individidual.setSigns(signs); // Добавляем признаки
            population.add(individidual); // Добавляем особь
        }
    }

    /**
     * Размножение (ОДНОТОЧЕЧНЫЙ ОПЕРАТОР КРОССИНГОВЕРА)
     * 1. Две хромосомы А = а1,а2,... , аL, и В = а1’,а2’,... ,aL’ выбираются  случайно из текущей популяции.
     * 2. Число k выбирается из {1,2, ...,L— 1} также случайно.
     * Здесь L — длина хромосомы, k — точка оператора кроссинговера
     * (номер, значение или код гена, после которого выполняется разрез хромосомы).
     * 3. Две новые хромосомы формируются из А и В путем перестановок.
     */
    public void crossoverBySinglePointOperator(){
        // 1. выбираются 2 особи случайно из текущей популяции
        // 1.1 для этого перемешаем arraуlist
        Collections.shuffle(population);

        // 1.2 и будем брать особи по порядку и попарно скрещивать
        Population result = new Population(); // здесь храним результат скрещивания, новые сооби
        for (int i = 0; i < population.size() - 1; i += 2){
            // берем родителей
            Individidual parent1 = population.get(i);
            Individidual parent2 = population.get(i + 1);

            // скрещиваем и получаем еще две особи
            Individidual[] children = Individidual.crossoverBySinglePoint(parent1, parent2);
            // вносим особи в популяцию
            // population.add(children[0]);
            // population.add(children[1]);
            result.addInd(children[0]);
            result.addInd(children[1]);
        }

        // После создание новыъ особей, добавим их в общую популяцию:
        population.addAll(result.getPopulation());
    }

    /**
     * Размножение (Двухточечный ОПЕРАТОР КРОССИНГОВЕРА)
     * В каждой хромосоме определяются две точки оператора кроссинговера,
     * и хромосомы обмениваются участками, расположенными между двумя точками оператора кроссинговера.
     * Точки оператора кроссинговера в двухточечном операторе кроссинговера также определяются случайно.
     */
    public void crossoverByTwoPointOperator(){
        // 1. выбираются 2 особи случайно из текущей популяции
        // 1.1 для этого перемешаем arraуlist
        Collections.shuffle(population);

        // 1.2 и будем брать особи по порядку и попарно скрещивать
        Population result = new Population(); // здесь храним результат скрещивания, новые сооби
        for (int i = 0; i < population.size() - 1; i += 2){
            // берем родителей
            Individidual parent1 = population.get(i);
            Individidual parent2 = population.get(i + 1);

            // скрещиваем и получаем еще две особи
            Individidual[] children = Individidual.crossoverByTwoPoints(parent1, parent2);
            // вносим особи в популяцию
            // population.add(children[0]);
            // population.add(children[1]);
            result.addInd(children[0]);
            result.addInd(children[1]);
        }

        // после того как посчитали новые особи добавляем их в общую популяцию:
        population.addAll(result.getPopulation());

    }

    public void crossoverByThreePointsOperator(){
        // 1. выбираются 2 особи случайно из текущей популяции
        // 1.1 для этого перемешаем arraуlist
        Collections.shuffle(population);

        // 1.2 и будем брать особи по порядку и попарно скрещивать
        Population result = new Population(); // здесь храним результат скрещивания, новые сооби
        for (int i = 0; i < population.size() - 1; i += 2){
            // берем родителей
            Individidual parent1 = population.get(i);
            Individidual parent2 = population.get(i + 1);

            // скрещиваем и получаем еще две особи
            Individidual[] children = Individidual.crossoverByThreePoints(parent1, parent2);
            // вносим особи в популяцию
            result.addInd(children[0]);
            result.addInd(children[1]);
        }

        // после того как посчитали новые особи добавляем их в общую популяцию:
        population.addAll(result.getPopulation());
    }

    /**
     * Метод для добавления особи в популяцию
     * @param ind - особь, которую нужно добавить
     */
    public void addInd(Individidual ind) {
        population.add(ind);
    }

    /**
     * Проводим мутацию для каждой особи
     */
    public void mutate(){
        for (Individidual el: population) {
            el.mutateInStepIncrease(func);
            // можно сделать любой метод
        }
    }

    /**
     * Метод для поиска и удаления максимально отдаленных особей. Сначала сортируем по эвклидову
     * расстоянию, а затем удаляем вторую часть с наибольшими значениями.
     */
    public void clear() {
        // здесь сортируем популяцию по возрастанию эвклидового расстояния
        sortByEuclid();

        // удаляем половину (элементы, которые идут после среднего)
        int mid = population.size() / 2; // индекс среднего элемента
        System.out.println("mid=" + mid);

        // формируем новую популяцию
        // в нее включаем половину особей (тех, у кого эвклидово расстояние минимально)
        ArrayList<Individidual> newPopulation = new ArrayList<>();
        for (int i = 0; i < mid; i++){
            newPopulation.add(population.get(i));
        }

        // теперь новая популяция заняла место старой:
        population = newPopulation;
    }

    /**
     * Функция для сортировки особей по возрастанию по эвклидовому расстоянию между
     * признаками особи и искомой функцией
     */
    public void sortByEuclid(){
        // считаем эвклидово расстояние для всех особей
        for (Individidual ind:
                population) {
            ind.countEuclidDistanceForSignsWithRequiredFunc(func); // сравниваем с искомой функцией
        }

        // здесь сортируем популяцию по возрастанию
        population.sort(new Comparator<Individidual>() {
            @Override
            public int compare(Individidual o1, Individidual o2) {
                return o1.compareTo(o2);
            }
        });
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();

        for (int i = 0; i < population.size(); i++){
            str.append(i).append(") ");
            str.append(population.get(i).toString());
            str.append("\n");
        }

        return str.toString();
    }

    /**
     * Выводит особь с минимальным эвклидовым расстоянием.
     * @return особь с наименьшим значением эвклидового расстояния среди всей популяции
     */
    public Individidual getMinEuclideanDistance(){
        // минимум = первому элементу
        Individidual indWithMinEuclid = population.get(0);

        // смотрим по всей популяции
        for (Individidual ind: population) {
            double minEuclidDist = indWithMinEuclid.getEuclideanDistance(); // смотрим минимум на данный момент
            if (ind.getEuclideanDistance() < minEuclidDist){ // если он меньше то это новый минимум
                indWithMinEuclid = ind;
            }
        }

        return indWithMinEuclid;
    }

    public List<Individidual> getPopulation() {
        return population;
    }

    public double[] getFunc() {
        return func;
    }

    public double[] getX() {
        return x;
    }

}
