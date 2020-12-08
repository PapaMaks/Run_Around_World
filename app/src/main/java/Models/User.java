package Models;
//Сюда будем помещать данные о пользователе
public class User {
    private String name, email, pass, phone; //Создаём переменные

    public User() {} //Создаём конструктор по умолчанию


    //Создаём конструктор
    public User(String name, String email, String pass, String phone) {
        this.name = name;
        this.email = email;
        this.pass = pass;
        this.phone = phone;
    }

    //Создаём конструктор для получения и установления
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
