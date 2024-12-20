<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="org.programmers.cocktail.search.dto.CocktailsTO" %>

<%
    CocktailsTO cocktailById = (CocktailsTO)request.getAttribute("cocktailById");
    Long cocktailId = cocktailById.getId();
    String cocktailName = cocktailById.getName();
    String ingredients = cocktailById.getIngredients();
    String recipes = cocktailById.getRecipes();
    String category = cocktailById.getCategory();
    String alcoholic = cocktailById.getAlcoholic();
    String imageUrl = cocktailById.getImage_url();
    Long hits = cocktailById.getHits();
    Long likes = cocktailById.getLikes();
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Cocktail Details</title>
    <link rel="stylesheet" href="/css/favorite.css">
    <script src="/js/favorite.js"></script>
</head>
<body>
<div>
    <h1><%= cocktailName %></h1>
    <img src="<%= imageUrl %>" alt="Cocktail Image" width="300">
    <p>Ingredients: <%= ingredients %></p>
    <p>Recipes: <%= recipes %></p>
    <p>Category: <%= category %></p>
    <p>Alcoholic: <%= alcoholic %></p>
    <p>Hits: <%= hits %></p>
</div>
<div>
    <span id="favoriteIcon" class="favorite">&#9825;</span>
    <span id="likeIcon" class="like">&#9829;</span>
</div>
<h2>Reviews</h2>
<div class="review-form">
    <textarea id="reviewText" placeholder="Write your review..."></textarea>
    <button id="submitReview">Submit Review</button>
</div>
<div class="review-list" id="reviewList">
    <!-- Reviews will load here -->
</div>
<script>
  const cocktailId = <%= cocktailId %>;
</script>

</body>
</html>
