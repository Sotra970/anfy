package anfy.com.anfy.Interface;

import anfy.com.anfy.Model.ArticleItem;

public interface ArticleCallbacks {

    void onFavChanged(ArticleItem articleItem);
    void onShare(ArticleItem articleItem);
}
