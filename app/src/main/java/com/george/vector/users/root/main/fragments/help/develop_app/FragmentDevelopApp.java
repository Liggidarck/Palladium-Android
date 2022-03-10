package com.george.vector.users.root.main.fragments.help.develop_app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.george.vector.R;
import com.george.vector.databinding.FragmentDevelopAppBinding;
import com.george.vector.users.root.main.fragments.help.FragmentAboutProject;

import java.util.ArrayList;

public class FragmentDevelopApp extends Fragment {

    FragmentDevelopAppBinding binding;
    ArrayList<Release> releases = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDevelopAppBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        releases.add(new Release("Palladium alfa.1", "13 июня 2021", "Основные изменения\n" +
                "\n" +
                "- Добавлен логин пользователя.\n" +
                "- Добавлена регистрация нового пользователя.\n" +
                "- У администратора появилась возможность создать новую заявку.\n" +
                "- Все заявки сортируются в зависимости от статуса.\n" +
                "- У пользователя показываются только его созданные заявки.\n" +
                "- Показываются для каждой заявки данные в отдельной Activity для пользователя.\n" +
                "- Показываются для каждой заявки данные в отдельной Activity для админа.\n" +
                "- Добавлена возможность админу отредактировать заявку.\n" +
                "- Добавлен список исполнителей.\n" +
                "- Добавлена возможность опционально оставлять комментарии к заявкам.\n" +
                "- Переработана страница профиль. Теперь это BottomSheet в котором находится основная информация о пользователе.\n" +
                "- В AdminBottomSheet появилась кнопка позволяющая выйти из аккаунта.\n" +
                "- У пользователя в BottomAppBar появилось меню настроек в виде BottomSheet, в котором можно выйти из аккаунта."));

        releases.add(new Release("Palladium alfa.2", "18 июня 2021", "Основные изменения\n" +
                "\n" +
                "- Теперь у админа появилась возможность отредактировать данные пользователя.\n" +
                "- Если нет комментариев к заявке, то не показывается текст в поле комментарий.\n" +
                "- Добавлена фильтрация в список пользователей. Теперь список можно отфильтровать по администраторам, пользователям, исполнителям.\n" +
                "- Все Toast заменены на логи.\n" +
                "- Добавлена проверку на пустоту во время изменения заявки.\n" +
                "- Добавлена глобальная проверку на подключение к интернету.\n" +
                "- Добавлена проверка на подключение к интернету во время создания заявки.\n" +
                "- Добавлена проверка на подключение к интернету во время изменения заявки."));

        releases.add(new Release("Palladium alfa.3", "30 июня 2021", "Основные изменения \n" +
                "\n" +
                "- Добавлена загрузка.\n" +
                "   - Создание/изменение заявок\n" +
                "   - Регистрирование пользователя\n" +
                "   - Изменение данных пользователя\n" +
                "   - Страница заявки\n" +
                "- Теперь можно к заявке прикрепить фото.\n" +
                "- Добавлена возможность пользователю менять основную информацию о себе.\n" +
                "- Добавлено удаление картинки из сервера.\n" +
                "- Создан новый дизайн логину."));

        releases.add(new Release("Palladium alfa.4", "6 июля 2021", "Основные изменения \n" +
                "\n" +
                "- Добавлено назначение рабочему заявки. (Root, админ, завхоз).\n" +
                "- Добавлена страница заявки исполнителя.\n" +
                "- Добавлена возможность исполнителю изменить статус заявки.\n" +
                "- Добавлена возможность рут, завхозу, админу удалить заявку.\n" +
                "- Добавлено для админа меню настроек.\n" +
                "- Изменена регистрация пользователей. Добавлено функциональное поле permission."));

        releases.add(new Release("Palladium alfa.5", "13 июля 2021", "Основные изменения\n" +
                "\n" +
                "- Добавлен загрузочный экран\n" +
                "- Hardcoded string в UI (-)\n" +
                "- Удалена свайп загрузка из списка пользователей\n" +
                "- В зависимости от выбранного места во время создания заявки, автоматически устанавливается адрес.\n"));

        releases.add(new Release("Palladium beta. 1", "15 июля 2021", "Основные изменения\n" +
                "\n" +
                "- Добавлена возможность создавать заявки барышевской школе\n" +
                "- Изменен алгоритм сохранения/изменения\n" +
                "- Активировано добавление фотки к заявке"));

        releases.add(new Release("Palladium beta. 2", "14 августа 2021", "Основные изменения\n" +
                "\n" +
                "- Добавлены иконки школе и детским садам.\n" +
                "- Создан новый дизайн меню профиль.\n" +
                "- Добавлен AlertDialog перед удалением заявки.\n" +
                "- Добавлена возможность восстановить забытый пароль.\n" +
                "- Изменено в профиле ИФО на ФИО.\n" +
                "- Заменен курсивный шрифт в комментариях, на тонкий.\n" +
                "- Переделано отображение ошибок у полей ввода текста.\n" +
                "- Исправлена опечатка в рут пользователе \"Новая заявка\" -> \"Новые заявки\".\n" +
                "- Добавлен поиск заявок.\n" +
                "- Добавлен новый дизайн главной страницы пользователя."));

        releases.add(new Release("Palladium beta. 3", "15 августа 2021", "Основные изменения\n" +
                "\n" +
                "- Добавлены настройки рут пользователю.\n" +
                "- Добавлена возможность завхозу создать заявки в барышевской школе.\n" +
                "- Добавлена проверка на подключение к интернету ко всем пользователям.\n" +
                "- Добавлены фильтры к заявкам рут пользователю.\n" +
                "- Добавлена загрузка ко всем пользователям.\n" +
                "- Новый дизайн главной страницы пользователя.\n" +
                "- Добавлен новостной фрагмент, который будет отображаться у пользователя по изменениям на сервере."));

        releases.add(new Release("Palladium beta. 4", "17 августа 2021", "Основные изменения\n" +
                "\n" +
                "- Переделано меню у исполнителя\n" +
                "- Добавлены настройки админу и завхозу\n" +
                "- Активирована работа фильтров заявок остафьевской школы у всех пользователей\n" +
                "- Добавлено новое текстовое поле - литера в создание заявки.\n" +
                "- Добавлены фильтры для всех пользователей барышевской школе"));

        releases.add(new Release("Palladium 1.0.1", "29 августа 2021", "Релизная версия Palladium"));


        releases.add(new Release("Palladium 1.0.2", "4 сентября 2021", "Срочность заявки\n" +
                "\n" +
                "- Добавлена новая настройка для заявки - срочность. Теперь заявку можно отметить как срочную.\n" +
                "- На главной странице новых заявок для остафьевской школы можно отсортировать заявки по срочности."));

        releases.add(new Release("Palladium 1.1.0", "6 сентября 2021", "Основные изменения\n" +
                "\n" +
                "- Создан новый дизайн рут пользователю.\n" +
                "- Добавлена новая настройка для заявки - срочность. Теперь заявку можно отметить как срочную.\n" +
                "- Теперь при создании/изменении заявки Root может назначить любому другому пользователю заявку на выполнение.\n" +
                "- Добавлена возможность прикрепить к заявке фотографию."));

        releases.add(new Release("Palladium 1.1.2", "8 сентября 2021", "Основные изменения\n" +
                "\n" +
                "- Теперь можно создать заявку без фотографии\n" +
                "- Изменена стоковая картинка\n" +
                "- Исправлен критический баг при создании пользователя\n" +
                "- Активирован просмотр заявок в барышевской роще\n" +
                "- Удалены неиспользуемые файлы.\n" +
                "- Теперь выход из настроек перекидывает в профиль\n" +
                "- Рут пользователю добавлена возможность посмотреть фотографию во весь экран"));

        releases.add(new Release("Palladium 1.1.3", "8 сентября 2021", "- Сделана новая система отрисовки списка заявок"));

        releases.add(new Release("Palladium 1.2.0", "17 октября 2021", "Основные изменения\n" +
                "\n" +
                "- Новая иконка приложения.\n" +
                "- Новый экран загрузки.\n" +
                "- Добавлена кнопка повернуть изображение заявки на 90°.\n" +
                "- В списке всех заявок отображается иконка срочной заявки.\n" +
                "- Скорость отрисовки изображения заявки увеличена вдвое.\n" +
                "- Добавлена новая настройка - \"Экономия трафика\". С включенным параметром приложение не будет заранее автоматически подгружать изображение заявки.\n" +
                "- Полностью переделана страница заявки.\n" +
                "- В редактировании данных пользователя суперпользователю добавлена возможность посмотреть пароль любого пользователя.\n" +
                "- Теперь можно поделиться заявкой через другие приложения.\n" +
                "- Переименованы Детские сады и Школы в ОП Остафьевская и ОП Барыши\n" +
                "- Добавлена трассировка приложения. Теперь я могу отслеживать время запуска приложения, время отправки на сервер данных и время получения данных из севера."));

        releases.add(new Release("Palladium 1.2.2", "2 ноября 2021", "Основные изменения\n" +
                "\n" +
                "- Исправлена регистрация пользователей\n" +
                "- Добавлена возможность сделать фотографию внутри приложения\n" +
                "- Добавлены уведомления суперпользователю о новой созданной заявке"));

        releases.add(new Release("Palladium 1.3", "9 марта 2022", "Основные изменения\n" + " - Испрвлены ошибки на странице авторизации. \n - Добавлена техническая поддержка для всех пользоватлей. \n - Добавлена страница о проекте"));

        ReleaseAdapter adapter = new ReleaseAdapter(FragmentDevelopApp.this.getActivity(), releases);
        binding.developRecycler.setAdapter(adapter);

        binding.developToolbar.setNavigationOnClickListener(v -> {
            Fragment aboutProject = new FragmentAboutProject();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_main_root, aboutProject).commit();
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
