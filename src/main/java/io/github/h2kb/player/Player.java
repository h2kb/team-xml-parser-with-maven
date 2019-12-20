package io.github.h2kb.player;

public class Player {
    private String name;
    private String surname;
    private int age;
    private String team;
    private String role;

    public Player(String name, String surname, int age, String team, String role) {
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.team = team;
        this.role = role;
    }

    @Override
    public String toString() {
        return surname + " " + name;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public int getAge() {
        return age;
    }

    public String getTeam() {
        return team;
    }

    public String getRole() {
        return role;
    }
}
