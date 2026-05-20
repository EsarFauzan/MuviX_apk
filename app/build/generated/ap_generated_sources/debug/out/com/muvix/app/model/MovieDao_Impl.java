package com.muvix.app.model;

import androidx.annotation.NonNull;
import androidx.room.EntityInsertAdapter;
import androidx.room.RoomDatabase;
import androidx.room.util.DBUtil;
import androidx.room.util.SQLiteStatementUtil;
import androidx.sqlite.SQLiteStatement;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation", "removal"})
public final class MovieDao_Impl implements MovieDao {
  private final RoomDatabase __db;

  private final EntityInsertAdapter<Movie> __insertAdapterOfMovie;

  public MovieDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertAdapterOfMovie = new EntityInsertAdapter<Movie>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `movies` (`id`,`title`,`posterUrl`,`bannerUrl`,`genre`,`episode`,`duration`,`views`,`description`,`rating`,`subscribed`,`watchedAt`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement, final Movie entity) {
        if (entity.id == null) {
          statement.bindNull(1);
        } else {
          statement.bindText(1, entity.id);
        }
        if (entity.title == null) {
          statement.bindNull(2);
        } else {
          statement.bindText(2, entity.title);
        }
        if (entity.posterUrl == null) {
          statement.bindNull(3);
        } else {
          statement.bindText(3, entity.posterUrl);
        }
        if (entity.bannerUrl == null) {
          statement.bindNull(4);
        } else {
          statement.bindText(4, entity.bannerUrl);
        }
        if (entity.genre == null) {
          statement.bindNull(5);
        } else {
          statement.bindText(5, entity.genre);
        }
        if (entity.episode == null) {
          statement.bindNull(6);
        } else {
          statement.bindText(6, entity.episode);
        }
        if (entity.duration == null) {
          statement.bindNull(7);
        } else {
          statement.bindText(7, entity.duration);
        }
        if (entity.views == null) {
          statement.bindNull(8);
        } else {
          statement.bindText(8, entity.views);
        }
        if (entity.description == null) {
          statement.bindNull(9);
        } else {
          statement.bindText(9, entity.description);
        }
        statement.bindDouble(10, entity.rating);
        final int _tmp = entity.subscribed ? 1 : 0;
        statement.bindLong(11, _tmp);
        statement.bindLong(12, entity.watchedAt);
      }
    };
  }

  @Override
  public void saveMovie(final Movie movie) {
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      __insertAdapterOfMovie.insert(_connection, movie);
      return null;
    });
  }

  @Override
  public Movie getMovieById(final String movieId) {
    final String _sql = "SELECT * FROM movies WHERE id = ? LIMIT 1";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        if (movieId == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, movieId);
        }
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfTitle = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "title");
        final int _columnIndexOfPosterUrl = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "posterUrl");
        final int _columnIndexOfBannerUrl = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "bannerUrl");
        final int _columnIndexOfGenre = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "genre");
        final int _columnIndexOfEpisode = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "episode");
        final int _columnIndexOfDuration = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "duration");
        final int _columnIndexOfViews = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "views");
        final int _columnIndexOfDescription = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "description");
        final int _columnIndexOfRating = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "rating");
        final int _columnIndexOfSubscribed = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "subscribed");
        final int _columnIndexOfWatchedAt = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "watchedAt");
        final Movie _result;
        if (_stmt.step()) {
          _result = new Movie();
          if (_stmt.isNull(_columnIndexOfId)) {
            _result.id = null;
          } else {
            _result.id = _stmt.getText(_columnIndexOfId);
          }
          if (_stmt.isNull(_columnIndexOfTitle)) {
            _result.title = null;
          } else {
            _result.title = _stmt.getText(_columnIndexOfTitle);
          }
          if (_stmt.isNull(_columnIndexOfPosterUrl)) {
            _result.posterUrl = null;
          } else {
            _result.posterUrl = _stmt.getText(_columnIndexOfPosterUrl);
          }
          if (_stmt.isNull(_columnIndexOfBannerUrl)) {
            _result.bannerUrl = null;
          } else {
            _result.bannerUrl = _stmt.getText(_columnIndexOfBannerUrl);
          }
          if (_stmt.isNull(_columnIndexOfGenre)) {
            _result.genre = null;
          } else {
            _result.genre = _stmt.getText(_columnIndexOfGenre);
          }
          if (_stmt.isNull(_columnIndexOfEpisode)) {
            _result.episode = null;
          } else {
            _result.episode = _stmt.getText(_columnIndexOfEpisode);
          }
          if (_stmt.isNull(_columnIndexOfDuration)) {
            _result.duration = null;
          } else {
            _result.duration = _stmt.getText(_columnIndexOfDuration);
          }
          if (_stmt.isNull(_columnIndexOfViews)) {
            _result.views = null;
          } else {
            _result.views = _stmt.getText(_columnIndexOfViews);
          }
          if (_stmt.isNull(_columnIndexOfDescription)) {
            _result.description = null;
          } else {
            _result.description = _stmt.getText(_columnIndexOfDescription);
          }
          _result.rating = _stmt.getDouble(_columnIndexOfRating);
          final int _tmp;
          _tmp = (int) (_stmt.getLong(_columnIndexOfSubscribed));
          _result.subscribed = _tmp != 0;
          _result.watchedAt = _stmt.getLong(_columnIndexOfWatchedAt);
        } else {
          _result = null;
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public List<Movie> getHistory() {
    final String _sql = "SELECT * FROM movies WHERE watchedAt > 0 ORDER BY watchedAt DESC";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfTitle = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "title");
        final int _columnIndexOfPosterUrl = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "posterUrl");
        final int _columnIndexOfBannerUrl = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "bannerUrl");
        final int _columnIndexOfGenre = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "genre");
        final int _columnIndexOfEpisode = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "episode");
        final int _columnIndexOfDuration = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "duration");
        final int _columnIndexOfViews = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "views");
        final int _columnIndexOfDescription = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "description");
        final int _columnIndexOfRating = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "rating");
        final int _columnIndexOfSubscribed = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "subscribed");
        final int _columnIndexOfWatchedAt = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "watchedAt");
        final List<Movie> _result = new ArrayList<Movie>();
        while (_stmt.step()) {
          final Movie _item;
          _item = new Movie();
          if (_stmt.isNull(_columnIndexOfId)) {
            _item.id = null;
          } else {
            _item.id = _stmt.getText(_columnIndexOfId);
          }
          if (_stmt.isNull(_columnIndexOfTitle)) {
            _item.title = null;
          } else {
            _item.title = _stmt.getText(_columnIndexOfTitle);
          }
          if (_stmt.isNull(_columnIndexOfPosterUrl)) {
            _item.posterUrl = null;
          } else {
            _item.posterUrl = _stmt.getText(_columnIndexOfPosterUrl);
          }
          if (_stmt.isNull(_columnIndexOfBannerUrl)) {
            _item.bannerUrl = null;
          } else {
            _item.bannerUrl = _stmt.getText(_columnIndexOfBannerUrl);
          }
          if (_stmt.isNull(_columnIndexOfGenre)) {
            _item.genre = null;
          } else {
            _item.genre = _stmt.getText(_columnIndexOfGenre);
          }
          if (_stmt.isNull(_columnIndexOfEpisode)) {
            _item.episode = null;
          } else {
            _item.episode = _stmt.getText(_columnIndexOfEpisode);
          }
          if (_stmt.isNull(_columnIndexOfDuration)) {
            _item.duration = null;
          } else {
            _item.duration = _stmt.getText(_columnIndexOfDuration);
          }
          if (_stmt.isNull(_columnIndexOfViews)) {
            _item.views = null;
          } else {
            _item.views = _stmt.getText(_columnIndexOfViews);
          }
          if (_stmt.isNull(_columnIndexOfDescription)) {
            _item.description = null;
          } else {
            _item.description = _stmt.getText(_columnIndexOfDescription);
          }
          _item.rating = _stmt.getDouble(_columnIndexOfRating);
          final int _tmp;
          _tmp = (int) (_stmt.getLong(_columnIndexOfSubscribed));
          _item.subscribed = _tmp != 0;
          _item.watchedAt = _stmt.getLong(_columnIndexOfWatchedAt);
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public List<Movie> getSubscribed() {
    final String _sql = "SELECT * FROM movies WHERE subscribed = 1 ORDER BY title ASC";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfTitle = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "title");
        final int _columnIndexOfPosterUrl = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "posterUrl");
        final int _columnIndexOfBannerUrl = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "bannerUrl");
        final int _columnIndexOfGenre = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "genre");
        final int _columnIndexOfEpisode = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "episode");
        final int _columnIndexOfDuration = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "duration");
        final int _columnIndexOfViews = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "views");
        final int _columnIndexOfDescription = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "description");
        final int _columnIndexOfRating = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "rating");
        final int _columnIndexOfSubscribed = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "subscribed");
        final int _columnIndexOfWatchedAt = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "watchedAt");
        final List<Movie> _result = new ArrayList<Movie>();
        while (_stmt.step()) {
          final Movie _item;
          _item = new Movie();
          if (_stmt.isNull(_columnIndexOfId)) {
            _item.id = null;
          } else {
            _item.id = _stmt.getText(_columnIndexOfId);
          }
          if (_stmt.isNull(_columnIndexOfTitle)) {
            _item.title = null;
          } else {
            _item.title = _stmt.getText(_columnIndexOfTitle);
          }
          if (_stmt.isNull(_columnIndexOfPosterUrl)) {
            _item.posterUrl = null;
          } else {
            _item.posterUrl = _stmt.getText(_columnIndexOfPosterUrl);
          }
          if (_stmt.isNull(_columnIndexOfBannerUrl)) {
            _item.bannerUrl = null;
          } else {
            _item.bannerUrl = _stmt.getText(_columnIndexOfBannerUrl);
          }
          if (_stmt.isNull(_columnIndexOfGenre)) {
            _item.genre = null;
          } else {
            _item.genre = _stmt.getText(_columnIndexOfGenre);
          }
          if (_stmt.isNull(_columnIndexOfEpisode)) {
            _item.episode = null;
          } else {
            _item.episode = _stmt.getText(_columnIndexOfEpisode);
          }
          if (_stmt.isNull(_columnIndexOfDuration)) {
            _item.duration = null;
          } else {
            _item.duration = _stmt.getText(_columnIndexOfDuration);
          }
          if (_stmt.isNull(_columnIndexOfViews)) {
            _item.views = null;
          } else {
            _item.views = _stmt.getText(_columnIndexOfViews);
          }
          if (_stmt.isNull(_columnIndexOfDescription)) {
            _item.description = null;
          } else {
            _item.description = _stmt.getText(_columnIndexOfDescription);
          }
          _item.rating = _stmt.getDouble(_columnIndexOfRating);
          final int _tmp;
          _tmp = (int) (_stmt.getLong(_columnIndexOfSubscribed));
          _item.subscribed = _tmp != 0;
          _item.watchedAt = _stmt.getLong(_columnIndexOfWatchedAt);
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public void clearHistory() {
    final String _sql = "UPDATE movies SET watchedAt = 0 WHERE watchedAt > 0";
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        _stmt.step();
        return null;
      } finally {
        _stmt.close();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
