package diplom.application.run_around_world;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button; //подключилас библиотека для связывания кнопок
import android.widget.RelativeLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import Models.User;

public class MainActivity extends AppCompatActivity {

    Button btnSignIn, btnRegister; //Вызывали класс Кнопка и создали переменные
    FirebaseAuth auth; //Добавили класс для авторизации и создали переменную
    FirebaseDatabase db; //Добавили класс для подключения к базе данных и создали переменную
    DatabaseReference users; //Добавили класс для работ с таблица в БД и создали переменную

    RelativeLayout root; //Добавлем переменную от класса RelativeLayout, для нахождения нашего activity_main


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSignIn = findViewById(R.id.btnSignIn);  //В переменную btnSignIn помещаем кнопку для входа(ссылка)
        btnRegister = findViewById(R.id.btnRegister); //В переменную btnRegister помещаем кнопку для регистрации(ссылка)

        root = findViewById(R.id.root_element); //Находим родительский элемент и передаём в переменную

        auth = FirebaseAuth.getInstance(); //Благодаря функции getInstance запускаем авторизацию в БД
        db = FirebaseDatabase.getInstance(); //Указываем с какой БД работаем, подключаемся (FireBase)
        users = db.getReference("Users"); //Указываем с какой таблицей работаем

        btnRegister.setOnClickListener(new View.OnClickListener() { //Установили для кнопки регистрации обработчик событий
            @Override
            public void onClick(View v) {
                //Этот код будет выполняться при нажатии на кнопку регистрации
                ShowRegisterWindow(); //вызываем функцию, которая реализована ниже
            }

        });

        btnSignIn.setOnClickListener(new View.OnClickListener() { //Установили для кнопки вход обработчик событий
            @Override
            public void onClick(View v) {
                //Этот код будет выполняться при нажатии на кнопку вход
                ShowSignInWindow(); //вызываем функцию, которая реализована ниже
            }

        });
    }

    private void ShowSignInWindow(){ //Функция, которая будет вызвана при нажатии на кнопку Вход

        AlertDialog.Builder dialog = new AlertDialog.Builder(this); //встроенный класс для отображения всплывающих окон и создали обхект dialog
        dialog.setTitle("Войти"); //Указываем заголовок для всплывающего окна
        dialog.setMessage("Введите данные для входа"); //Указываем подпись под заголовком

        LayoutInflater inflater = LayoutInflater.from(this); //для получения шаблона, который создали ранее, чтобы в всплывающее окно поместить. Создаём класс In Falter
        View sign_in_window = inflater.inflate(R.layout.sign_in_window, null); //создали переменную на основе класса View и помещаем наш ранее созданный шаблон
        dialog.setView(sign_in_window); // устанавливаем шаблон для всплывающего окна

        //Получаем данные текстовых полей в нашем шаблоне layout register_window
        MaterialEditText email = sign_in_window.findViewById(R.id.emailField); // создаём объект email на основе класса MaterialEditText
        MaterialEditText pass = sign_in_window.findViewById(R.id.passField); // создаём объект pass на основе класса MaterialEditText

        //Создаём кнопки Отменить(Назад) и кнопку Отправить(и зарегестрировать пользователя в БД)
        dialog.setNegativeButton("Отменить", new DialogInterface.OnClickListener() { // функция setNegativeButton для установления кнопки Отмена. Первый параметр отображаемый текст. Второй парамер - функция при нажатии на кнопку
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss(); //Скрывает при нажатии
            }
        });

        dialog.setPositiveButton("Войти", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) { //названия параметров поменяли чтобы не повторялись
                if (TextUtils.isEmpty(email.getText().toString())) { //Класс TextUtils даёт доступ к функции isEmpty, проверка на пустое поле
                    Snackbar.make(root, "Введите вашу почту", Snackbar.LENGTH_SHORT).show(); //обращаемся к классу Snackbar для вызова всплывающего окна. LENGTH_SHORT - отвечает за длительность = 3 сек
                    return;
                }

                if (pass.getText().toString().length() < 5) { //Класс TextUtils даёт доступ к функции isEmpty, проверка на пустое поле
                    Snackbar.make(root, "Введите пароль, который более 5 символов", Snackbar.LENGTH_SHORT).show(); //обращаемся к классу Snackbar для вызова всплывающего окна. LENGTH_SHORT - отвечает за длительность = 3 сек
                    return;
                }

                //Авторизация по Email и паролю
                auth.signInWithEmailAndPassword(email.getText().toString(), pass.getText().toString())
                        //Вызываем обработчик события
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) { //В случае успешной авторизации вызывается onSuccess
                               //Перебрасываем пользователя на новую страницу Activity
                                startActivity(new Intent(MainActivity.this, MapActivity.class));
                                finish();
                            }
                            //В случае неуспещной авторизации
                        }).addOnFailureListener(new OnFailureListener() { //В случае неуспешной авторизации
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Вызываем высплывающее окно при неуспешной авторизации
                        Snackbar.make(root,"Ошибка авторизации" + e.getMessage(),Snackbar.LENGTH_SHORT).show();
                    }
                });
            }

        });

        dialog.show();

    }

    private void ShowRegisterWindow() { //Добавили метод из контекстного меню после создания функции ShowRegisterWindow
        AlertDialog.Builder dialog = new AlertDialog.Builder(this); //встроенный класс для отображения всплывающих окон и создали обхект dialog
        dialog.setTitle("Зарегестрироваться"); //Указываем заголовок для всплывающего окна
        dialog.setMessage("Введите все данные для регистрации"); //Указываем подпись под заголовком

        LayoutInflater inflater = LayoutInflater.from(this); //для получения шаблона, который создали ранее, чтобы в всплывающее окно поместить. Создаём класс In Falter
        View register_window = inflater.inflate(R.layout.register_window, null); //создали переменную на основе класса View и помещаем наш ранее созданный шаблон
        dialog.setView(register_window); // устанавливаем шаблон для всплывающего окна

        //Получаем данные текстовых полей в нашем шаблоне layout register_window
        MaterialEditText email = register_window.findViewById(R.id.emailField); // создаём объект email на основе класса MaterialEditText
        MaterialEditText pass = register_window.findViewById(R.id.passField); // создаём объект pass на основе класса MaterialEditText
        MaterialEditText name = register_window.findViewById(R.id.nameField); // создаём объект name на основе класса MaterialEditText
        MaterialEditText phone = register_window.findViewById(R.id.phoneField); // создаём объект phone на основе класса MaterialEditText

        //Создаём кнопки Отменить(Назад) и кнопку Отправить(и зарегестрировать пользователя в БД)
        dialog.setNegativeButton("Отмениь", new DialogInterface.OnClickListener() { // функция setNegativeButton для установления кнопки Отмена. Первый параметр отображаемый текст. Второй парамер - функция при нажатии на кнопку
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss(); //Скрывает при нажатии
            }
        });

        dialog.setPositiveButton("Добавить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) { //названия параметров поменяли чтобы не повторялись
                if (TextUtils.isEmpty(email.getText().toString())) { //Класс TextUtils даёт доступ к функции isEmpty, проверка на пустое поле
                    Snackbar.make(root, "Введите вашу почту", Snackbar.LENGTH_SHORT).show(); //обращаемся к классу Snackbar для вызова всплывающего окна. LENGTH_SHORT - отвечает за длительность = 3 сек
                    return;
                }

                if (TextUtils.isEmpty(name.getText().toString())) { //Класс TextUtils даёт доступ к функции isEmpty, проверка на пустое поле
                    Snackbar.make(root, "Введите ваше имя", Snackbar.LENGTH_SHORT).show(); //обращаемся к классу Snackbar для вызова всплывающего окна. LENGTH_SHORT - отвечает за длительность = 3 сек
                    return;
                }

                if (TextUtils.isEmpty(phone.getText().toString())) { //Класс TextUtils даёт доступ к функции isEmpty, проверка на пустое поле
                    Snackbar.make(root, "Введите ваш телефон", Snackbar.LENGTH_SHORT).show(); //обращаемся к классу Snackbar для вызова всплывающего окна. LENGTH_SHORT - отвечает за длительность = 3 сек
                    return;
                }

                if (pass.getText().toString().length() < 5) { //Класс TextUtils даёт доступ к функции isEmpty, проверка на пустое поле
                    Snackbar.make(root, "Введите пароль, который более 5 символов", Snackbar.LENGTH_SHORT).show(); //обращаемся к классу Snackbar для вызова всплывающего окна. LENGTH_SHORT - отвечает за длительность = 3 сек
                    return;
                }

                //Регистрация пользователя
                auth.createUserWithEmailAndPassword(email.getText().toString(), pass.getText().toString())
                      .addOnSuccessListener(new OnSuccessListener<AuthResult>() { //Вызываем функцию addOnSuccessListener. В случае успешной регистрации вызовет обработчик событий
                          @Override
                          public void onSuccess(AuthResult authResult) {
                              User user = new User();//Создаём объект на основе класса созданного нами (класс подключили alt + Enter) + создаём нового пользователя
                              user.setEmail(email.getText().toString()); //Передаём параметры
                              user.setName(name.getText().toString());
                              user.setPass(pass.getText().toString());
                              user.setPhone(phone.getText().toString());

                              //Передаём наш объект в базу данных
                              users.child(FirebaseAuth.getInstance().getCurrentUser().getUid()) //Используем переменную users и обращаемся к функции child, которая идентифицирует пользователя по email (ключ пользователя)
                                      .setValue(user) // В таблицу user добавили нашего нового пользователя

                                      //Обработчик событий вызовется в слуае успешного добавления пользователя
                                      .addOnSuccessListener(new OnSuccessListener<Void>() {
                                          @Override
                                          public void onSuccess(Void aVoid) {
                                              Snackbar.make(root,"Пользователь добавлен", Snackbar.LENGTH_SHORT).show();
                                          }
                                      });

                          }
                      });
            }
        });

        dialog.show();
    }


}