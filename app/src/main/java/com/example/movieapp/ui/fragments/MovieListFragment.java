package com.example.movieapp.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieapp.R;
import com.example.movieapp.data.database.AppDatabase;
import com.example.movieapp.data.model.Category;
import com.example.movieapp.data.model.Movie;
import com.example.movieapp.ui.adapter.CategoryAdapter;
import com.example.movieapp.ui.adapters.MovieAdapter;
import com.example.movieapp.ui.movie.MovieDetailActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MovieListFragment extends Fragment implements CategoryAdapter.OnCategoryClickListener {
    
    private RecyclerView recyclerView;
    private RecyclerView recyclerCategories;
    private ProgressBar progressBar;
    private LinearLayout emptyState;
    private ImageButton btnSearch;
    private TextInputLayout searchLayout;
    private TextInputEditText searchEditText;
    
    private MovieAdapter adapter;
    private CategoryAdapter categoryAdapter;
    private AppDatabase database;
    private ExecutorService executor;
    private List<Movie> allMovies;
    private List<Movie> filteredMovies;
    private List<Category> categories;
    
    private String currentSearchQuery = "";
    private String currentGenreFilter = "";
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_list, container, false);
        
        database = AppDatabase.getInstance(requireContext());
        executor = Executors.newSingleThreadExecutor();
        allMovies = new ArrayList<>();
        filteredMovies = new ArrayList<>();
        
        initViews(view);
        setupCategories();
        setupRecyclerViews();
        setupSearch();
        
        // Initialiser la liste vide pour éviter les erreurs
        filteredMovies.clear();
        if (adapter != null) {
            adapter.updateMovies(filteredMovies);
        }
        
        loadMovies();
        
        return view;
    }
    
    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerCategories = view.findViewById(R.id.recycler_categories);
        progressBar = view.findViewById(R.id.progress_bar);
        emptyState = view.findViewById(R.id.empty_state);
        btnSearch = view.findViewById(R.id.btn_search);
        searchLayout = view.findViewById(R.id.search_layout);
        searchEditText = view.findViewById(R.id.search_edit_text);
    }
    
    private void setupCategories() {
        categories = new ArrayList<>();
        categories.add(new Category("Tous", ""));
        categories.add(new Category("Action", "Action"));
        categories.add(new Category("Aventure", "Aventure"));
        categories.add(new Category("Comédie", "Comédie"));
        categories.add(new Category("Drame", "Drame"));
        categories.add(new Category("Horreur", "Horreur"));
        categories.add(new Category("Romance", "Romance"));
        categories.add(new Category("Science-Fiction", "Science-Fiction"));
        categories.add(new Category("Thriller", "Thriller"));
        categories.add(new Category("Animation", "Animation"));
        categories.add(new Category("Documentaire", "Documentaire"));
        categories.add(new Category("Crime", "Crime"));
        categories.add(new Category("Fantastique", "Fantastique"));
        categories.add(new Category("Guerre", "Guerre"));
        categories.add(new Category("Historique", "Historique"));
        categories.add(new Category("Musical", "Musical"));
        categories.add(new Category("Mystère", "Mystère"));
        categories.add(new Category("Western", "Western"));
        
        // Sélectionner "Tous" par défaut
        categories.get(0).setSelected(true);
    }
    
    private void setupRecyclerViews() {
        // Setup movies RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new MovieAdapter(requireContext(), filteredMovies, new MovieAdapter.OnMovieClickListener() {
            @Override
            public void onRentClick(Movie movie) {
                // Gérer le clic sur louer
                // Vous pouvez ajouter votre logique ici
            }
            
            @Override
            public void onTrailerClick(Movie movie) {
                // Ouvrir les détails du film
                Intent intent = new Intent(requireContext(), MovieDetailActivity.class);
                intent.putExtra("movie_id", movie.getId());
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);
        
        // Setup categories RecyclerView
        recyclerCategories.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        categoryAdapter = new CategoryAdapter(categories, this);
        recyclerCategories.setAdapter(categoryAdapter);
    }
    
    private void setupSearch() {
        btnSearch.setOnClickListener(v -> {
            if (searchLayout.getVisibility() == View.GONE) {
                searchLayout.setVisibility(View.VISIBLE);
                searchEditText.requestFocus();
            } else {
                searchLayout.setVisibility(View.GONE);
                searchEditText.setText("");
                searchEditText.clearFocus();
            }
        });
        
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                currentSearchQuery = s.toString().trim();
                filterMovies();
            }
            
            @Override
            public void afterTextChanged(Editable s) {}
        });
        
        searchEditText.setOnEditorActionListener((v, actionId, event) -> {
            searchEditText.clearFocus();
            return true;
        });
    }
    
    @Override
    public void onCategoryClick(Category category) {
        // android.util.Log.d("CategoryClick", "Catégorie: " + category.getName());
        
        int position = categories.indexOf(category);
        if (categoryAdapter != null) {
            categoryAdapter.updateSelection(position);
        }
        
        currentGenreFilter = category.getGenre();
        filterMovies();
    }
    
    private void filterMovies() {
        filteredMovies.clear();
        
        for (Movie movie : allMovies) {
            boolean matchesSearch = currentSearchQuery.isEmpty() || 
                    movie.getTitle().toLowerCase().contains(currentSearchQuery.toLowerCase()) ||
                    (movie.getDescription() != null && movie.getDescription().toLowerCase().contains(currentSearchQuery.toLowerCase()));
            
            boolean matchesGenre = currentGenreFilter.isEmpty() || 
                    (movie.getGenre() != null && movie.getGenre().equalsIgnoreCase(currentGenreFilter));
            
            if (matchesSearch && matchesGenre) {
                filteredMovies.add(movie);
            }
        }
        
        // Logging pour debug (peut être supprimé en production)
        // android.util.Log.d("MovieFilter", "Filter: '" + currentGenreFilter + "' -> " + filteredMovies.size() + " films trouvés");
        
        if (adapter != null) {
            adapter.updateMovies(new ArrayList<>(filteredMovies));
        }
        updateEmptyState();
    }
    
    private void updateEmptyState() {
        if (filteredMovies.isEmpty()) {
            emptyState.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            emptyState.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }
    
    private void loadMovies() {
        showProgress(true);
        
        executor.execute(() -> {
            try {
                List<Movie> movieList = database.movieDao().getAllMovies();
                
                // Si la base est vide, ajouter des films de démonstration
                if (movieList.isEmpty()) {
                    addDemoMovies();
                    movieList = database.movieDao().getAllMovies();
                }
                
                final List<Movie> finalMovieList = movieList;
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        showProgress(false);
                        allMovies.clear();
                        allMovies.addAll(finalMovieList);
                        filterMovies();
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        showProgress(false);
                        Toast.makeText(requireContext(), "Erreur lors du chargement des films", Toast.LENGTH_SHORT).show();
                    });
                }
            }
        });
    }
    
    private void addDemoMovies() {
        List<Movie> demoMovies = new ArrayList<>();
        
        // Films récents 2024 avec vraies images
        demoMovies.add(new Movie(
            "Dune: Part Two",
            "Science-Fiction",
            2024,
            "Anglais",
            "États-Unis",
            "Denis Villeneuve",
            "Timothée Chalamet, Zendaya, Rebecca Ferguson",
            "https://image.tmdb.org/t/p/w780/1pdfLvkbY9ohJlCjQH2CZjjYVvJ.jpg",
            "https://www.youtube.com/watch?v=Way9Dexny3w",
            8,
            "Paul Atreides s'unit avec Chani et les Fremen pour venger la conspiration qui a détruit sa famille."
        ));
        
        demoMovies.add(new Movie(
            "Oppenheimer",
            "Drame",
            2023,
            "Anglais",
            "États-Unis",
            "Christopher Nolan",
            "Cillian Murphy, Emily Blunt, Robert Downey Jr.",
            "https://image.tmdb.org/t/p/w780/8Gxv8gSFCU0XGDykEGv7zR1n2ua.jpg",
            "https://www.youtube.com/watch?v=uYPbbksJxIg",
            6,
            "L'histoire de J. Robert Oppenheimer et le développement de la bombe atomique."
        ));
        
        demoMovies.add(new Movie(
            "Barbie",
            "Comédie",
            2023,
            "Anglais",
            "États-Unis",
            "Greta Gerwig",
            "Margot Robbie, Ryan Gosling, America Ferrera",
            "https://image.tmdb.org/t/p/w780/iuFNMS8U5cb6xfzi51Dbkovj7vM.jpg",
            "https://www.youtube.com/watch?v=pBk4NYhWNMM",
            10,
            "Barbie vit dans Barbie Land. Un jour, elle est expulsée pour être moins que parfaite."
        ));
        
        demoMovies.add(new Movie(
            "Spider-Man: Across the Spider-Verse",
            "Animation",
            2023,
            "Anglais",
            "États-Unis",
            "Joaquim Dos Santos, Kemp Powers, Justin K. Thompson",
            "Shameik Moore, Hailee Steinfeld, Brian Tyree Henry",
            "https://image.tmdb.org/t/p/w780/8Vt6mWEReuy4Of61Lnj5Xj704m8.jpg",
            "https://www.youtube.com/watch?v=cqGjhVJWtEg",
            7,
            "Miles Morales voyage à travers le multivers pour sauver Gwen Stacy."
        ));
        
        demoMovies.add(new Movie(
            "Guardians of the Galaxy Vol. 3",
            "Action",
            2023,
            "Anglais",
            "États-Unis",
            "James Gunn",
            "Chris Pratt, Zoe Saldana, Dave Bautista",
            "https://image.tmdb.org/t/p/w780/r2J02Z2OpNTctfOSN1Ydgii51I3.jpg",
            "https://www.youtube.com/watch?v=u3V5KDHRQvk",
            9,
            "Peter Quill doit rassembler son équipe pour défendre l'univers et protéger l'un des siens."
        ));
        
        demoMovies.add(new Movie(
            "John Wick: Chapter 4",
            "Action",
            2023,
            "Anglais",
            "États-Unis",
            "Chad Stahelski",
            "Keanu Reeves, Donnie Yen, Bill Skarsgård",
            "https://image.tmdb.org/t/p/w780/vZloFAK7NmvMGKE7VkF5UHaz0I.jpg",
            "https://www.youtube.com/watch?v=qEVUtrk8_B4",
            5,
            "John Wick découvre un moyen de vaincre la Grande Table."
        ));
        
        demoMovies.add(new Movie(
            "Avatar: The Way of Water",
            "Science-Fiction",
            2022,
            "Anglais",
            "États-Unis",
            "James Cameron",
            "Sam Worthington, Zoe Saldana, Sigourney Weaver",
            "https://image.tmdb.org/t/p/w780/t6HIqrRAclMCA60NsSmeqe9RmNV.jpg",
            "https://www.youtube.com/watch?v=d9MyW72ELq0",
            12,
            "Jake Sully et Neytiri forment une famille et font tout pour rester ensemble."
        ));
        
        demoMovies.add(new Movie(
            "Top Gun: Maverick",
            "Action",
            2022,
            "Anglais",
            "États-Unis",
            "Joseph Kosinski",
            "Tom Cruise, Jennifer Connelly, Miles Teller",
            "https://image.tmdb.org/t/p/w780/62HCnUTziyWcpDaBO2i1DX17ljH.jpg",
            "https://www.youtube.com/watch?v=giXco2jaZ_4",
            8,
            "Maverick forme une nouvelle génération de pilotes pour une mission dangereuse."
        ));
        
        demoMovies.add(new Movie(
            "The Batman",
            "Action",
            2022,
            "Anglais",
            "États-Unis",
            "Matt Reeves",
            "Robert Pattinson, Zoë Kravitz, Paul Dano",
            "https://image.tmdb.org/t/p/w780/b0PlSFdDwbyK0cf5RxwDpaOJQvQ.jpg",
            "https://www.youtube.com/watch?v=mqqft2x_Aa4",
            6,
            "Batman traque le Riddler dans les rues sombres de Gotham City."
        ));
        
        demoMovies.add(new Movie(
            "Scream VI",
            "Horreur",
            2023,
            "Anglais",
            "États-Unis",
            "Matt Bettinelli-Olpin, Tyler Gillett",
            "Melissa Barrera, Jenna Ortega, Jasmin Savoy Brown",
            "https://image.tmdb.org/t/p/w780/wDWwtvkRRlgTiUr6TyLSMX8FCuZ.jpg",
            "https://www.youtube.com/watch?v=h74AXqw4Opc",
            4,
            "Les survivants de Ghostface quittent Woodsboro pour un nouveau chapitre."
        ));
        
        demoMovies.add(new Movie(
            "Fast X",
            "Action",
            2023,
            "Anglais",
            "États-Unis",
            "Louis Leterrier",
            "Vin Diesel, Michelle Rodriguez, Jason Statham",
            "https://image.tmdb.org/t/p/w780/fiVW06jE7z9YnO4trhaMEdclSiC.jpg",
            "https://www.youtube.com/watch?v=aOb15GVFZxU",
            7,
            "Dom Toretto et sa famille font face à un ennemi du passé."
        ));
        
        demoMovies.add(new Movie(
            "The Super Mario Bros. Movie",
            "Animation",
            2023,
            "Anglais",
            "États-Unis",
            "Aaron Horvath, Michael Jelenic",
            "Chris Pratt, Anya Taylor-Joy, Charlie Day",
            "https://image.tmdb.org/t/p/w780/qNBAXBIQlnOThrVvA6mA2B5ggV6.jpg",
            "https://www.youtube.com/watch?v=TnGl01FkMMo",
            15,
            "Mario et Luigi voyagent dans le royaume champignon pour sauver la princesse Peach."
        ));
        
        demoMovies.add(new Movie(
            "Transformers: Rise of the Beasts",
            "Action",
            2023,
            "Anglais",
            "États-Unis",
            "Steven Caple Jr.",
            "Anthony Ramos, Dominique Fishback, Luna Lauren Vélez",
            "https://image.tmdb.org/t/p/w780/gPbM0MK8CP8A174rmUwGsADNYKD.jpg",
            "https://www.youtube.com/watch?v=itnqEauWQZM",
            5,
            "Les Autobots s'allient aux Maximals pour sauver la Terre."
        ));
        
        demoMovies.add(new Movie(
            "Indiana Jones and the Dial of Destiny",
            "Aventure",
            2023,
            "Anglais",
            "États-Unis",
            "James Mangold",
            "Harrison Ford, Phoebe Waller-Bridge, Antonio Banderas",
            "https://image.tmdb.org/t/p/w780/Af4bXE63pVsb2FtbW8uYIyPBadD.jpg",
            "https://www.youtube.com/watch?v=eQfMbSe7F2w",
            6,
            "Indiana Jones se lance dans une dernière aventure épique."
        ));
        
        demoMovies.add(new Movie(
            "M3GAN",
            "Horreur",
            2023,
            "Anglais",
            "États-Unis",
            "Gerard Johnstone",
            "Allison Williams, Violet McGraw, Ronny Chieng",
            "https://image.tmdb.org/t/p/w780/d9nBoowhjiiYc4FBNtQkPY7c11H.jpg",
            "https://www.youtube.com/watch?v=BRb4U99OU80",
            3,
            "Une poupée IA devient violemment surprotectrice de sa jeune propriétaire."
        ));
        
        demoMovies.add(new Movie(
            "Ant-Man and the Wasp: Quantumania",
            "Action",
            2023,
            "Anglais",
            "États-Unis",
            "Peyton Reed",
            "Paul Rudd, Evangeline Lilly, Michael Douglas",
            "https://image.tmdb.org/t/p/w780/ngl2FKBlU4fhbdsrtdom9LVLBXw.jpg",
            "https://www.youtube.com/watch?v=ZlNFpri-Y40",
            8,
            "Scott Lang et Hope van Dyne explorent le royaume quantique."
        ));
        
        demoMovies.add(new Movie(
            "Cocaine Bear",
            "Comédie",
            2023,
            "Anglais",
            "États-Unis",
            "Elizabeth Banks",
            "Keri Russell, O'Shea Jackson Jr., Christian Convery",
            "https://image.tmdb.org/t/p/w780/gOnmaxHo0412UVr1QM5Nekv1xPi.jpg",
            "https://www.youtube.com/watch?v=DuKOqkWXdoY",
            2,
            "Un ours noir américain consomme de la cocaïne et devient incontrôlable."
        ));
        
        demoMovies.add(new Movie(
            "Evil Dead Rise",
            "Horreur",
            2023,
            "Anglais",
            "États-Unis",
            "Lee Cronin",
            "Lily Sullivan, Alyssa Sutherland, Morgan Davies",
            "https://image.tmdb.org/t/p/w780/5ik4ATKmNtmJU6AYD0bLm56BCVM.jpg",
            "https://www.youtube.com/watch?v=BYlMjgTjvAc",
            4,
            "Deux sœurs se retrouvent piégées avec des démons dans un immeuble."
        ));
        
        demoMovies.add(new Movie(
            "The Little Mermaid",
            "Musical",
            2023,
            "Anglais",
            "États-Unis",
            "Rob Marshall",
            "Halle Bailey, Jonah Hauer-King, Melissa McCarthy",
            "https://image.tmdb.org/t/p/w780/ym1dxyOk4jFcSl4Q2zmRrA5BEEN.jpg",
            "https://www.youtube.com/watch?v=kpGo2_d3oYE",
            11,
            "Ariel rêve de vivre dans le monde des humains."
        ));
        
        demoMovies.add(new Movie(
            "Killers of the Flower Moon",
            "Crime",
            2023,
            "Anglais",
            "États-Unis",
            "Martin Scorsese",
            "Leonardo DiCaprio, Robert De Niro, Lily Gladstone",
            "https://image.tmdb.org/t/p/w780/dB6Krk806zeqd0YNp2ngQ9zXteH.jpg",
            "https://www.youtube.com/watch?v=7cx9nCHsemc",
            3,
            "Les meurtres mystérieux de membres de la nation Osage dans l'Oklahoma."
        ));
        
        // Ajouter les films à la base de données
        for (Movie movie : demoMovies) {
            database.movieDao().insertMovie(movie);
        }
    }
    
    private void showProgress(boolean show) {
        if (show) {
            progressBar.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            emptyState.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            updateEmptyState();
        }
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (executor != null) {
            executor.shutdown();
        }
    }
} 