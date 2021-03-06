package me.walmyrcarvalho.recrutamentoandroid.ui.seasondetail;

import java.util.List;

import me.walmyrcarvalho.recrutamentoandroid.data.api.TraktClient;
import me.walmyrcarvalho.recrutamentoandroid.data.api.callback.ShowDetailCallback;
import me.walmyrcarvalho.recrutamentoandroid.data.api.callback.ShowRatingCallback;
import me.walmyrcarvalho.recrutamentoandroid.data.api.callback.ShowSeasonCallback;
import me.walmyrcarvalho.recrutamentoandroid.data.entity.Episode;
import me.walmyrcarvalho.recrutamentoandroid.data.entity.Rating;
import me.walmyrcarvalho.recrutamentoandroid.data.entity.Show;

import static me.walmyrcarvalho.recrutamentoandroid.misc.util.Preconditions.checkNotNull;

public class SeasonDetailPresenter implements
        SeasonDetailContract.Presenter, ShowSeasonCallback, ShowDetailCallback, ShowRatingCallback {

    private SeasonDetailContract.View view;
    private TraktClient client;

    public SeasonDetailPresenter(SeasonDetailContract.View view, TraktClient client) {
        this.view = checkNotNull(view, "SeasonDetailContract.View cannot be null.");
        this.client = checkNotNull(client, "TraktClient cannot be null.");
    }

    @Override
    public void loadShowSeason(String showName, int season) {
        if (view != null) {
            view.showProgress();
        }
        client.loadShowSeason(showName, season, this);
    }

    @Override
    public void loadShowDetail(String showName) {
        client.loadShowDetail(showName, this);
    }

    @Override
    public void loadShowRating(String showName) {
        client.loadShowRating(showName, this);
    }

    @Override
    public void attachView(SeasonDetailContract.View view) {
        this.view = view;
    }

    @Override
    public void dettachView() {
        view = null;
    }

    @Override
    public void onShowSeasonLoaded(List<Episode> episodes) {
        if (view != null) {
            view.hideProgress();
            view.showSeasonEpisodes(episodes);
        }
    }

    @Override
    public void onShowSeasonNotLoaded(String errorMessage) {
        if (view != null) {
            view.hideProgress();
            view.showError(errorMessage);
        }
    }

    @Override
    public void onShowDetailLoaded(Show show) {
        if (view != null) {
            if (show.images.poster.url != null) {
                view.showPosterImage(show.images.poster.url);
            }

            if (show.images.banner.url != null) {
                view.showHeaderImage(show.images.banner.url);
            }
        }
    }

    @Override
    public void onShowDetailNotLoaded(String errorMessage) {
        if (view != null) {
            view.showError(errorMessage);
        }
    }

    @Override
    public void onShowRatingLoaded(Rating rating) {
        if (view != null) {
            view.showRating(rating.getPrettyRating());
        }
    }

    @Override
    public void onShowRatingNotLoaded(String errorMessage) {
        if (view != null) {
            view.showError(errorMessage);
        }
    }
}
