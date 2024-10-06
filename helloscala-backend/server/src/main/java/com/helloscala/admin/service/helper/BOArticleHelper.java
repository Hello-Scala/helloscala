package com.helloscala.admin.service.helper;

import com.helloscala.admin.controller.view.BOArticleView;
import com.helloscala.common.utils.ListHelper;
import com.helloscala.service.web.view.ArticleView;
import org.jetbrains.annotations.NotNull;

/**
 * @author stevezou
 */
public final class BOArticleHelper {
 public static @NotNull BOArticleView buildBoArticleView(ArticleView article) {
  BOArticleView articleView = new BOArticleView();
  articleView.setId(article.getId());
  articleView.setUserId(article.getUserId());
  articleView.setTitle(article.getTitle());
  articleView.setNickname(article.getNickname());
  articleView.setAvatar(article.getAvatar());
  articleView.setReadType(article.getReadType());
  articleView.setIsStick(article.getIsStick());
  articleView.setIsOriginal(article.getIsOriginal());
  articleView.setQuantity(article.getQuantity());
  articleView.setCreateTime(article.getCreateTime());
  articleView.setIsPublish(article.getIsPublish());
  articleView.setCategoryName(article.getCategoryName());
  articleView.setTagNames(String.join(",", ListHelper.ofNullable(article.getTagNames())));
  articleView.setTagList(article.getTagNames());
  return articleView;
 }
}
