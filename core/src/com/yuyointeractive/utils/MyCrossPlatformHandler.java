package com.yuyointeractive.utils;

public interface MyCrossPlatformHandler {
  // public void loadingRank();
  //
  // public void rank();
  //
  // public void share();
  //
  // public void ktplay();
  //
  // public void buyOne();
  //
  // public void buyTwo();
  //
  // public void buyRemoveAds();
  //
  // public void showBannerAd();
  //
  // public void hideBannerAd();
  //
  // public void showPopAd();
  //
  // public void orderQuer();
  //
  public void quit();
  // public void login(Gamer gamer, OnLoginListener listener);
  public void loginWeChat(MyNetworkListener listener);
  public void loginAlipay();
  public void shareWeChatTimeLine(String title, String description);
  public void shareWeChatSession(String title, String description);
  public void shareAlipay(String str);
  public void startRecordVoice(String id);
  public void playVoice(String id);
  public void connectRongCloud(String token);
  public void createDiscussion(String roomId, String myUserId);
  public void addMemberToDiscussion(String userId);
  public void stopRecordVoice(String roomId,String voiceId);
}
