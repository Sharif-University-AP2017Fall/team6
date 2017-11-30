public class Bank {

    private int burrowedMoney;
    private int dueDate;

    void lendMoney(Hero hero, int request) {
        hero.addMoney(request);
        this.burrowedMoney = request;
    }

    boolean isDue(int currentDate) {
        return currentDate >= dueDate;
    }

    boolean payBack(Hero hero) {
        if (hero.getMoney() >= (int) (burrowedMoney * 1.5)) {
            hero.reduceMoney((int) (burrowedMoney * 1.5));
            return true;
        } else {
            return false;
        }
    }

    void setDueDate(int dueDate) {
        this.dueDate = dueDate;
    }

    int getDueDate() {
        return dueDate;
    }
}
