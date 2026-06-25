package com.example.craftblox;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends Activity {

    private WebView gameView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1. Прячем панель уведомлений и статус-бар телефона (полноэкранный режим)
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        // 2. Инициализируем WebView контейнер
        gameView = new WebView(this);
        setContentView(gameView);

        // 3. Конфигурируем движок WebView под запуск тяжелого JS кода
        WebSettings webSettings = gameView.getSettings();
        webSettings.setJavaScriptEnabled(true);        // Включаем поддержку скриптов игры
        webSettings.setDomStorageEnabled(true);         // Разрешаем локальное кеширование движка
        webSettings.setAllowFileAccess(true);           // Даем доступ к внутренним ассетам игры
        webSettings.setAllowContentAccess(true);
        
        // Предотвращаем открытие системного браузера при переходах
        gameView.setWebViewClient(new WebViewClient());

        // Выключаем скролл-бары (чтобы не дергался экран при свайпах)
        gameView.setVerticalScrollBarEnabled(false);
        gameView.setHorizontalScrollBarEnabled(false);

        // 4. Загружаем локальный движок игры
        gameView.loadUrl("file:///android_asset/index.html");
    }

    // Обработка системной кнопки "Назад" на телефоне
    @Override
    public void onBackPressed() {
        if (gameView.canGoBack()) {
            gameView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
