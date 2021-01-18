package SecondMidterm.FootballTable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

class Team {
    private String name;
    private int games;
    private int win;
    private int draw;
    private int lose;
    private int points;
    private int scoredGoals;
    private int concededGoals;
    public static Comparator<Team> teamComparator = Comparator.comparingInt(Team::getPoints)
            .thenComparing(Team::goalsDifference)
            .reversed()
            .thenComparing(Team::getName);

    public Team(String name) {
        this.name = name;
        this.games = 0;
        this.win = 0;
        this.draw = 0;
        this.lose = 0;
        this.concededGoals = 0;
        this.scoredGoals = 0;
        this.points = 0;
    }

    public void updateStats(int win, int dray, int lose, int points, int scoredGoals, int concededGoals) {
        this.games += 1;
        this.win += win;
        this.draw += dray;
        this.lose += lose;
        this.points += points;
        this.scoredGoals += scoredGoals;
        this.concededGoals += concededGoals;
    }

    public String getName() {
        return name;
    }

    public int getLose() {
        return lose;
    }

    public int getPoints() {
        return points;
    }

    public int goalsDifference() {
        return scoredGoals - concededGoals;
    }

    @Override
    public String toString() {
        return String.format("%-15s%5d%5d%5d%5d%5d", name, games, win, draw, lose, points);
    }
}

class FootballTable {
    private HashMap<String, Team> teamTable;

    public FootballTable() {
        teamTable = new HashMap<>();
    }

    public void addGame(String homeTeam, String awayTeam, int homeGoals, int awayGoals) {
        teamTable.putIfAbsent(homeTeam, new Team(homeTeam));
        teamTable.putIfAbsent(awayTeam, new Team(awayTeam));

        if(homeGoals > awayGoals) {
            teamTable.get(homeTeam).updateStats(1, 0, 0, 3, homeGoals, awayGoals);
            teamTable.get(awayTeam).updateStats(0, 0, 1, 0, awayGoals, homeGoals);
        }
        if(homeGoals < awayGoals) {
            teamTable.get(homeTeam).updateStats(0, 0, 1, 0, homeGoals, awayGoals);
            teamTable.get(awayTeam).updateStats(1, 0, 0, 3, awayGoals, homeGoals);
        }
        if(homeGoals == awayGoals){
            teamTable.get(homeTeam).updateStats(0, 1, 0, 1, homeGoals, awayGoals);
            teamTable.get(awayTeam).updateStats(0, 1, 0, 1, awayGoals, homeGoals);
        }
    }

    public void printTable() {
        List<Team> tmp = teamTable.values()
                .stream()
                .sorted(Team.teamComparator)
                .collect(Collectors.toList());
        int i = 1;
        for (Team t : tmp) {
            System.out.printf("%2d. %s\n",i, t);
            i++;
        }
    }
}

public class FootballTableTest {
    public static void main(String[] args) throws IOException {
        FootballTable table = new FootballTable();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        reader.lines()
                .map(line -> line.split(";"))
                .forEach(parts -> table.addGame(parts[0], parts[1],
                        Integer.parseInt(parts[2]),
                        Integer.parseInt(parts[3])));
        reader.close();
        System.out.println("=== TABLE ===");
        System.out.printf("%-19s%5s%5s%5s%5s%5s\n", "Team", "P", "W", "D", "L", "PTS");
        table.printTable();
    }
}
