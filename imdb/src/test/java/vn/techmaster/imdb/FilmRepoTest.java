package vn.techmaster.imdb;

import java.util.*;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import vn.techmaster.imdb.model.Film;
import vn.techmaster.imdb.repository.FilmRepository;

@SpringBootTest
class FilmRepoTest {
	@Autowired private FilmRepository filmRepo;

	@Test
	public void getAll() {
		List<Film> filmList = filmRepo.getAll();
	}

	@Test
	@DisplayName("getFilmByCountry ok")
	public void getFilmByCountry() {
		// TODO Auto-generated method stub
		Map<String, List<Film>> result= filmRepo.getFilmByCountry();
		System.out.println(result.toString());

		List<String> countries= filmRepo.getAll().
				stream().
				map(Film::getCountry).
				distinct().
				collect(Collectors.toList());

		Assertions.assertTrue( countries.containsAll(result.keySet()) );
	}

	@Test
	@DisplayName("getcountryMakeMostFilms ok")
	public void getcountryMakeMostFilms() {
		// TODO Auto-generated method stub
		Map.Entry<String, Integer> result= filmRepo.getcountryMakeMostFilms();

		List<Film> films= filmRepo.getAll();
		Map<String, Integer> mapOfCountryByFilmCount= new HashMap<>();
		for(Film film: films){
			mapOfCountryByFilmCount.put(
					film.getCountry(),
					mapOfCountryByFilmCount.get(film.getCountry()) == null ? 1 :
							mapOfCountryByFilmCount.get(film.getCountry())+1
					);
		}

		Map.Entry<String, Integer> maxEntry = null;

		for (Map.Entry<String, Integer> entry : mapOfCountryByFilmCount.entrySet())
		{
			if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0)
			{
				maxEntry = entry;
			}
		}

		Assertions.assertEquals(maxEntry, result);

		System.out.println(maxEntry.toString());
		System.out.println(result.toString());
	}

	@Test
	@DisplayName("yearMakeMostFilms ok")
	public void yearMakeMostFilms() {
		// TODO Auto-generated method stub
		Map.Entry<Integer, Integer> result= filmRepo.yearMakeMostFilms();

		List<Film> films= filmRepo.getAll();
		Map<Integer, Integer> mapOfYearByFilmCount= new HashMap<>();
		for(Film film: films){
			mapOfYearByFilmCount.put(
					film.getYear(),
					mapOfYearByFilmCount.get(film.getYear()) == null ? 1 :
							mapOfYearByFilmCount.get(film.getYear())+1
			);
		}

		Map.Entry<Integer, Integer> maxEntry = null;

		for (Map.Entry<Integer, Integer> entry : mapOfYearByFilmCount.entrySet())
		{
			if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0)
			{
				maxEntry = entry;
			}
		}

		Assertions.assertEquals(maxEntry, result);


		System.out.println(result.toString());
		System.out.println(maxEntry.toString());
	}

	@Test
	@DisplayName("getAllGeneres ok")
	public void getAllGeneres() {
		// TODO Auto-generated method stub
		List<String> result= filmRepo.getAllGeneres();

		List<Film> films= filmRepo.getAll();
		Map<List<String>, Integer> mapOfYearByFilmCount= new HashMap<>();
		for(Film film: films){
			mapOfYearByFilmCount.put(
					film.getGeneres(),
					mapOfYearByFilmCount.get(film.getGeneres()) == null ? 1 :
							mapOfYearByFilmCount.get(film.getGeneres())+1
			);
		}

		Set<List<String>> list= mapOfYearByFilmCount.keySet();
		Map<String, Integer> genreList= new HashMap<>();
		for(List<String> element: list){
			for(String str: element){
				genreList.put(
						str,
						genreList.get(str) == null ? 1 :
								genreList.get(str)+1);
			}
		}

		Assertions.assertTrue(genreList.keySet().stream().collect(Collectors.toList()).containsAll(result));

		System.out.println(result.toString());
		System.out.println(genreList.toString());
	}

	@Test
	@DisplayName("getFilmsMadeByCountryFromYearToYear ok")
	public void getFilmsMadeByCountryFromYearToYear() {
		// TODO Auto-generated method stub
		List<Film> result= filmRepo.getFilmsMadeByCountryFromYearToYear("China", 1980, 1999);

		List<Film> temp= filmRepo.getAll();
		ArrayList<Film> result2 = new ArrayList<>(temp);

		result2.removeIf(film ->
				film.getYear() < 1980 ||
				film.getYear() > 1999 ||
				!film.getCountry().equals("China"));

		System.out.println(result.toString());
		System.out.println(result2.toString());
	}

	@Test
	@DisplayName("categorizeFilmByGenere ok")
	public void categorizeFilmByGenere() {
		// TODO Auto-generated method stub
		filmRepo.categorizeFilmByGenere();
	}

	@Test
	@DisplayName("top5HighMarginFilms ok")
	public void top5HighMarginFilms() {
		// TODO Auto-generated method stub
		List<Film> result = filmRepo.top5HighMarginFilms();

		List<Film> result2= filmRepo.getAll();

		

		System.out.println(result.toString());
	}

	@Test
	@DisplayName("top5HighMarginFilmsIn1990to2000 ok")
	public void top5HighMarginFilmsIn1990to2000() {
		// TODO Auto-generated method stub
		List<Film> result = filmRepo.top5HighMarginFilmsIn1990to2000();
		System.out.println(result.toString());
	}

	@Test
	@DisplayName("ratioBetweenGenere ok")
	public void ratioBetweenGenere() {
		// TODO Auto-generated method stub
		 Double result= filmRepo.ratioBetweenGenere("fiction", "series");
		System.out.println(result.toString());
	}

	@Test
	@DisplayName("top5FilmsHighRatingButLowMargin ok")
	public void top5FilmsHighRatingButLowMargin() {
		// TODO Auto-generated method stub
		List<Film> result= filmRepo.top5FilmsHighRatingButLowMargin();
		System.out.println(result.toString());
	}
}
