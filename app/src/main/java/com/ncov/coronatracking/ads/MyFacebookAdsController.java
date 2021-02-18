package com.ncov.coronatracking.ads;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.facebook.ads.AbstractAdListener;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdIconView;
import com.facebook.ads.AdOptionsView;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdListener;
import com.facebook.ads.NativeBannerAd;
import com.ncov.coronatracking.R;
import java.util.ArrayList;
import java.util.List;

public class MyFacebookAdsController {

    private static InterstitialAd mInterstitialAd;

    public static int flagQC = 1;

    public static void initInterstitialAds(final Context ac) {
        // hiển thị quảng cáo
        // quảng cáo full màn hình
        if (mInterstitialAd == null)
            mInterstitialAd = new InterstitialAd(ac,AdsConfig.getFacebookInterId());


        mInterstitialAd.setAdListener(new AbstractAdListener() {
            @Override
            public void onInterstitialDismissed(Ad ad) {
                requestNewInterstitial();
            }
            @Override
            public void onError(Ad ad, AdError error) {
                super.onError(ad, error);
            }

        });

        if(!isInterLoaded())
            requestNewInterstitial();

    }

    private static void requestNewInterstitial() {
        mInterstitialAd.loadAd();
    }


    public static boolean isInterLoaded(){
        return mInterstitialAd!=null&&mInterstitialAd.isAdLoaded();
    }

    public static void showAdsFullBeforeDoAction(final Context context, final Callback callback) {

        if (mInterstitialAd == null) {
            callback.callBack(0, 0);
            return;
        }

        if (flagQC == 1) {
            if (mInterstitialAd.isAdLoaded()) {
                mInterstitialAd.setAdListener(new AbstractAdListener() {
                    @Override
                    public void onInterstitialDismissed(Ad ad) {
                        requestNewInterstitial();
                        flagQC = 0;
                        handler.removeCallbacks(runnable);
                        handler.postDelayed(runnable,AdsConfig.getTimeDelay());
                        callback.callBack(0, 0);
                    }
                    @Override
                    public void onError(Ad ad, AdError error) {
                        super.onError(ad, error);
                    }

                });
                mInterstitialAd.show();


            } else {
                callback.callBack(0, 0);

                requestNewInterstitial();
            }

        } else {
            callback.callBack(0, 0);

            requestNewInterstitial();
        }
    }

    public static void showAdsFullNow(final Context context, final Callback callback) {

        if (mInterstitialAd == null) {
            callback.callBack(0, 0);
            return;
        }
        if (mInterstitialAd.isAdLoaded()) {
            mInterstitialAd.setAdListener(new AbstractAdListener() {
                @Override
                public void onInterstitialDismissed(Ad ad) {
                    super.onInterstitialDismissed(ad);
                    requestNewInterstitial();
                    flagQC = 0;
                    handler.removeCallbacks(runnable);
                    handler.postDelayed(runnable,AdsConfig.getTimeDelay());
                    callback.callBack(0, 0);
                }

                @Override
                public void onError(Ad ad, AdError error) {
                    super.onError(ad, error);
                }

            });

            mInterstitialAd.show();


        } else {
            callback.callBack(0, 0);

            requestNewInterstitial();
        }
    }

    public static void initBannerAds(final Activity ctx) {

        final AdView adView = new AdView(ctx,AdsConfig.getFacebookBannerId(), AdSize.BANNER_HEIGHT_50);

        final RelativeLayout adViewContainer = ctx.findViewById(R.id.adView_container);
        adViewContainer.removeAllViews();

        try {
            adViewContainer.addView(adView);
        } catch (Exception e) {
            e.getMessage();
        }

        adView.setAdListener(new AbstractAdListener() {
            @Override
            public void onAdLoaded(Ad ad) {
                super.onAdLoaded(ad);
                adViewContainer.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError(Ad ad, AdError error) {
                super.onError(ad, error);
                adViewContainer.setVisibility(View.GONE);
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                super.onInterstitialDismissed(ad);
                adView.loadAd();
            }
        });
        adView.loadAd();

    }


    public static void initBannerQuickAccess(final View ctx) {

        final AdView adView = new AdView(ctx.getContext(),AdsConfig.getFacebookBannerQuickAccessId(), AdSize.RECTANGLE_HEIGHT_250);


        final RelativeLayout adViewContainer = ctx.findViewById(R.id.adView_container);
        adViewContainer.removeAllViews();

        try {
            adViewContainer.addView(adView);
        } catch (Exception e) {
            e.getMessage();
        }

        adView.setAdListener(new AbstractAdListener() {
            @Override
            public void onAdLoaded(Ad ad) {
                super.onAdLoaded(ad);
                adViewContainer.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError(Ad ad, AdError error) {
                super.onError(ad, error);
                adViewContainer.setVisibility(View.GONE);
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                super.onInterstitialDismissed(ad);
                adView.loadAd();
            }
        });
        adView.loadAd();

    }

    public static void initNativeMain(View view) {
        final NativeBannerAd nativeBannerAd = new NativeBannerAd(view.getContext(), AdsConfig.getFacebookNativeId());
        final View adsView = view.findViewById(R.id.viewAdsNativeFacebook);
        nativeBannerAd.setAdListener(new NativeAdListener() {
            @Override
            public void onMediaDownloaded(Ad ad) {

            }

            @Override
            public void onError(Ad ad, AdError adError) {
                adsView.setVisibility(View.GONE);
            }

            @Override
            public void onAdLoaded(Ad ad) {
                adsView.setVisibility(View.VISIBLE);
                inflateAd(nativeBannerAd, (NativeAdLayout) adsView.findViewById(R.id.native_banner_ad_container));
            }

            @Override
            public void onAdClicked(Ad ad) {
                adsView.setVisibility(View.GONE);
            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }
        });
        nativeBannerAd.loadAd();

    }

    private static void inflateAd(NativeBannerAd nativeBannerAd, NativeAdLayout nativeAdLayout) {
        // Unregister last ad
        nativeBannerAd.unregisterView();

        // Add the AdChoices icon
        RelativeLayout adChoicesContainer = nativeAdLayout.findViewById(R.id.ad_choices_container);
        AdOptionsView adOptionsView = new AdOptionsView(nativeAdLayout.getContext(), nativeBannerAd, nativeAdLayout);
        adChoicesContainer.removeAllViews();
        adChoicesContainer.addView(adOptionsView, 0);

        // Create native UI using the ad metadata.
        TextView nativeAdTitle = nativeAdLayout.findViewById(R.id.native_ad_title);
        TextView nativeAdSocialContext = nativeAdLayout.findViewById(R.id.native_ad_social_context);
        TextView sponsoredLabel = nativeAdLayout.findViewById(R.id.native_ad_sponsored_label);
        AdIconView nativeAdIconView = nativeAdLayout.findViewById(R.id.native_icon_view);
        Button nativeAdCallToAction = nativeAdLayout.findViewById(R.id.native_ad_call_to_action);

        // Set the Text.
        nativeAdCallToAction.setText(nativeBannerAd.getAdCallToAction());
        nativeAdCallToAction.setVisibility(
                nativeBannerAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
        nativeAdTitle.setText(nativeBannerAd.getAdvertiserName());
        nativeAdSocialContext.setText(nativeBannerAd.getAdSocialContext());
        sponsoredLabel.setText(nativeBannerAd.getSponsoredTranslation());

        // Register the Title and CTA button to listen for clicks.
        List<View> clickableViews = new ArrayList<>();
        clickableViews.add(nativeAdTitle);
        clickableViews.add(nativeAdCallToAction);
        nativeBannerAd.registerViewForInteraction(nativeAdLayout, nativeAdIconView, clickableViews);
    }

    private static Handler handler = new Handler();
    private  static Runnable runnable = new Runnable() {
        @Override
        public void run() {
            flagQC = 1;
        }
    };
}
