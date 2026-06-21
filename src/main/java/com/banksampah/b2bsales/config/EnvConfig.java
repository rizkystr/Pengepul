package com.banksampah.b2bsales.config;

import io.github.cdimascio.dotenv.Dotenv;

public class EnvConfig {
    private static final Dotenv dotenv;

    static {
        // Membaca konfigurasi .env.local yang berada di root project
        dotenv = Dotenv.configure().filename(".env.local").load();
    }

    public static String get(String key) {
        return dotenv.get(key);
    }
}
