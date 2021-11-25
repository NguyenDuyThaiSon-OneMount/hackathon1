package vn.techmaster.imdb.repository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.util.ResourceUtils;

import vn.techmaster.imdb.model.Film;

@Repository
public class FilmRepository implements IFilmRepo{
  private List<Film> films;

  public FilmRepository(@Value("${datafile}") String datafile) {
    try {
      File file = ResourceUtils.getFile("classpath:static/" + datafile);
      ObjectMapper mapper = new ObjectMapper(); // Dùng để ánh xạ cột trong CSV với từng trường trong POJO
      films = Arrays.asList(mapper.readValue(file, Film[].class));
    } catch (IOException e) {
      System.out.println(e);
    }
  }

  public List<Film> getAll() {
    return films;
  }

  @Override
  public Map<String, List<Film>> getFilmByCountry() {
    // TODO Auto-generated method stub
    Map<String, List<Film>> result= films.stream().collect(Collectors.groupingBy(Film::getCountry));

    return result;
  }

  @Override
  public Entry<String, Integer> getcountryMakeMostFilms() {
    // TODO Auto-generated method stub
    Map<String, Long> filmByCountryCount= films.stream().collect(Collectors.groupingBy(Film::getCountry, Collectors.counting()));

    Entry<String, Long> temp;
    temp= Collections.max(filmByCountryCount.entrySet(), (e1, e2) -> (int) (e1.getValue()- e2.getValue()));

    Entry<String, Integer> entry=  Map.entry(temp.getKey(), temp.getValue().intValue());

    return entry;
  }

  @Override
  public Entry<Integer, Integer> yearMakeMostFilms() {
    // TODO Auto-generated method stub
    Map<Integer, Long> filmByCountryCount= films.stream().collect(Collectors.groupingBy(Film::getYear, Collectors.counting()));

    Entry<Integer, Long> temp;
    temp= Collections.max(filmByCountryCount.entrySet(), (e1, e2) -> (int) (e1.getValue()- e2.getValue()));

    Entry<Integer, Integer> entry=  Map.entry(temp.getKey(), temp.getValue().intValue());

    return entry;
  }

  @Override
  public List<String> getAllGeneres() {
    // TODO Auto-generated method stub
    List<String> result= films.stream().
            collect(Collectors.groupingBy(Film::getGeneres, Collectors.counting())).
            keySet().stream().
            flatMap(x -> x.stream()).distinct().collect(Collectors.toList());

    return result;
  }

  @Override
  public List<Film> getFilmsMadeByCountryFromYearToYear(String country, int fromYear, int toYear) {
    // TODO Auto-generated method stub
    List<Film> result= films.stream().
            filter(c -> c.getCountry().equals(country) && c.getYear() >= fromYear && c.getYear() <= toYear).
            collect(Collectors.toList());

    return result;
  }

  @Override
  public Map<String, List<Film>> categorizeFilmByGenere() {
    // TODO Auto-generated method stub
    Map<List<String>, List<Film>> result= films.stream().collect(Collectors.groupingBy(Film::getGeneres));
    System.out.println(result);
//    Map<String, List<Film>> result2= result.entrySet().stream().flatMap(s -> s.getKey().stream().distinct());
    return null;
  }

  @Override
  public List<Film> top5HighMarginFilms() {
    // TODO Auto-generated method stub
    List<Film> result= films.stream().
            sorted(Comparator.comparingInt(e -> (e.getRevenue() - e.getCost()))).
            limit(5).
            collect(Collectors.toList());
    return result;
  }

  @Override
  public List<Film> top5HighMarginFilmsIn1990to2000() {
    // TODO Auto-generated method stub
    List<Film> result= films.stream().
            filter(e-> e.getYear()>=1990 && e.getYear() <= 2000).
            sorted(Comparator.comparingInt(e -> (e.getRevenue() - e.getCost()))).
            limit(5).
            collect(Collectors.toList());
    return result;
  }

  @Override
  public double ratioBetweenGenere(String genreX, String genreY) {
    // TODO Auto-generated method stub
    Map<String, Long> result= films.stream().collect(Collectors.groupingBy(Film::getGeneres, Collectors.counting())).keySet()
            .stream().flatMap(x -> x.stream()).collect(Collectors.groupingBy(x -> x ,Collectors.counting()));

    if(result.get(genreX)!= null && result.get(genreY)!= null){
      Double xCount= result.get(genreX).doubleValue();
      Double yCount= result.get(genreY).doubleValue();

      return xCount/yCount;
    }
    else return -1; //error value
  }

  @Override
  public List<Film> top5FilmsHighRatingButLowMargin() {
    // TODO Auto-generated method stub
    List<Film> result= films.stream().
            filter(e-> e.getRevenue()- e.getCost() < -2000).
            sorted(Comparator.comparingDouble(Film::getRating)).
            limit(5).
            collect(Collectors.toList());
    return result;
  }

}
