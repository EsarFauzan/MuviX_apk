package com.muvix.app.model;

import java.util.ArrayList;

public class SampleData {
    public static final String SWAPPED_ID = "swapped_special";

    public static ArrayList<Movie> movies() {
        ArrayList<Movie> list = new ArrayList<>();

        list.add(new Movie("1", "Interstellar", 
                "https://image.tmdb.org/t/p/w500/gEU2QniE6E77NI6lCU6MxlNBvIx.jpg",
                "https://image.tmdb.org/t/p/w780/rAiYTfKGqDCRIIqo664sY9XZIvQ.jpg",
                "Sci-Fi", "Movie", "169 min", "432K views",
                "Petualangan luar angkasa tentang cinta, waktu, dan harapan manusia.", 8.7));

        list.add(new Movie("2", "Inception",
                "https://image.tmdb.org/t/p/w500/edv5CZvWj09upOsy2Y6IwDhK8bt.jpg",
                "https://image.tmdb.org/t/p/w780/s3TBrRGB1iav7gFOCNx3H31MoES.jpg",
                "Action", "Movie", "148 min", "560K views",
                "Misi mencuri ide melalui mimpi berlapis yang bikin otak ikut gym.", 8.8));

        list.add(new Movie("3", "Dune",
                "https://image.tmdb.org/t/p/w500/d5NXSklXo0qyIYkgV94XAgMIckC.jpg",
                "https://image.tmdb.org/t/p/w780/jYEW5xZkZk2WTrdbMGAPFuBqbDc.jpg",
                "Adventure", "Movie", "155 min", "390K views",
                "Perebutan planet gurun Arrakis dan rempah paling berharga di semesta.", 8.1));

        list.add(new Movie("4", "The Batman",
                "https://image.tmdb.org/t/p/w500/74xTEgt7R36Fpooo50r9T25onhq.jpg",
                "https://image.tmdb.org/t/p/w780/b0PlSFdDwbyK0cf5RxwDpaOJQvQ.jpg",
                "Crime", "Movie", "176 min", "480K views",
                "Detektif gelap Gotham mengejar teka-teki kriminal yang brutal.", 7.8));

        list.add(new Movie("5", "Spider-Man: Across the Spider-Verse",
                "https://image.tmdb.org/t/p/w500/8Vt6mWEReuy4Of61Lnj5Xj704m8.jpg",
                "https://image.tmdb.org/t/p/w780/4HodYYKEIsGOdinkGi2Ucz6X9i0.jpg",
                "Animation", "Movie", "140 min", "690K views",
                "Miles Morales kembali melintasi multiverse dengan visual yang brutal bagusnya.", 8.6));

        list.add(new Movie("6", "Top Gun: Maverick",
                "https://image.tmdb.org/t/p/w500/62HCnUTziyWcpDaBO2i1DX17ljH.jpg",
                "https://image.tmdb.org/t/p/w780/odJ4hx6g6vBt4lBWKFD1tI8WS4x.jpg",
                "Action", "Movie", "130 min", "510K views",
                "Aksi pilot tempur dengan rasa nostalgia dan adrenalin tinggi.", 8.2));

        list.add(swappedMovie());

        return list;
    }

    public static Movie swappedMovie() {
        return new Movie(
                SWAPPED_ID,
                "Swapped",
                "local:sampul_swapped.jpeg",
                "local:sampul_swapped.jpeg",
                "Romance",
                "Movie",
                "110 min",
                "New Release",
                "Kisah dua orang yang hidupnya berubah setelah kejadian tak terduga.",
                8.0
        );
    }
}
