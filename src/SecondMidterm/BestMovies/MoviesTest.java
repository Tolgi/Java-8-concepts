package SecondMidterm.BestMovies;

import java.util.*;
import java.util.stream.Collectors;

class Movie {
    private String title;
    private int[] ratings;

    public Movie(String title, int[] ratings) {
        this.title = title;
        this.ratings = ratings;
    }

    public double getAverageRaiting() {
        return Arrays.stream(ratings)
                .average()
                .orElse(0.0);
    }

    public String getTitle() {
        return title;
    }

    public int[] getRatings() {
        return ratings;
    }

    public int numberOfRatings() {
        return ratings.length;
    }

    public double getBestRaitingCoef(int maxNumberOfRatings) {
        return getAverageRaiting() * numberOfRatings() * maxNumberOfRatings;
    }

    @Override
    public String toString() {
        return String.format("%s (%d) of %d ratings", title, getAverageRaiting(), numberOfRatings());
    }
}

class MovieFactory {
    public static Movie getMovieInstance(String title, int[] raitings) {
        return new Movie(title, raitings);
    }
}

class MoviesList {
    private List<Movie> movies;

    public MoviesList() {
        movies = new ArrayList<>();
    }

    public void addMovie(String title, int[] ratings) {
        movies.add(MovieFactory.getMovieInstance(title, ratings));
    }

    public List<Movie> top10ByAvgRating() {
        return movies.stream()
                .limit(10)
                .sorted(Comparator.comparingDouble(Movie::getAverageRaiting).reversed().thenComparing(Movie::getTitle))
                .collect(Collectors.toList());
    }

    public List<Movie> top10ByRatingCoef() {
        return movies.stream()
                .limit(10)
                .sorted(Comparator.comparingDouble(m -> m.getBestRaitingCoef(getMaxNumOfAllRatings())))
                .collect(Collectors.toList());
    }


    private int getMaxNumOfAllRatings() {
        return movies.stream()
                .mapToInt(Movie::numberOfRatings)
                .sum();
    }





}

public class MovieTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MoviesList moviesList = new MoviesList();
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            int x = scanner.nextInt();
            int[] ratings = new int[x];
            for (int j = 0; j < x; ++j) {
                ratings[j] = scanner.nextInt();
            }
            scanner.nextLine();
            moviesList.addMovie(title, ratings);
        }
        scanner.close();
        List<Movie> movies = moviesList.top10ByAvgRating();
        System.out.println("=== TOP 10 BY AVERAGE RATING ===");
        for (Movie movie : movies) {
            System.out.println(movie);
        }
        movies = moviesList.top10ByRatingCoef();
        System.out.println("=== TOP 10 BY RATING COEFFICIENT ===");
        for (Movie movie : movies) {
            System.out.println(movie);
        }
    }
}
