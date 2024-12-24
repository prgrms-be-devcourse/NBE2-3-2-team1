package org.programmers.cocktail.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity(name = "cocktails")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Cocktails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 600)
    private String ingredients;

    @Column(nullable = false, length = 600)
    private String recipes;

    @Column(nullable = true)
    private String category;

    @Column(nullable = true)
    private String alcoholic;

    @Column(nullable = true)
    private String image_url;

    @Column(nullable = false)// 초기 default값은 0
    private Long hits;

    @Column(nullable = false)// 초기 default값은 0
    private Long likes;

    @OneToMany(mappedBy = "cocktails", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comments> comments = new ArrayList<>();

    @OneToMany(mappedBy = "cocktails", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CocktailLists> cocktailLists = new ArrayList<>();

    @Builder
    public Cocktails(String name, String ingredients, String recipes, String category, String alcoholic, String image_url, Long hits, Long likes) {
        this.name = name;
        this.ingredients = ingredients;
        this.recipes = recipes;
        this.category = category;
        this.alcoholic = alcoholic;
        this.image_url = image_url;
        this.hits = hits;
        this.likes = likes;
    }

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;



}
