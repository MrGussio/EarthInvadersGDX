	package ga.gussio.ld38.earthinvaders;

	import android.graphics.Bitmap;
	import android.graphics.BitmapFactory;
	import android.os.AsyncTask;
	import android.os.Bundle;
	import android.support.v4.app.FragmentActivity;
	import android.view.View;
	import android.widget.Button;
	import android.widget.ImageView;
	import android.widget.TextView;

	import com.appnext.appnextsdk.API.AppnextAPI;
	import com.appnext.appnextsdk.API.AppnextAd;
	import com.appnext.appnextsdk.API.AppnextAdRequest;
	import com.appnext.base.Appnext;
	import com.appnext.core.AppnextError;
	import com.badlogic.gdx.Gdx;
	import com.badlogic.gdx.backends.android.AndroidFragmentApplication;

	import java.net.URL;
	import java.util.ArrayList;

	public class AndroidLauncher extends FragmentActivity implements AndroidFragmentApplication.Callbacks, Advertisements {

		private AppnextAPI api;

		private TextView adTitle;
		private TextView adRating;
		private ImageView adIcon;
		private Button adButton;
		private ImageView privacyButton;

		private View adContainer;

		@Override
		public void onCreate (Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);

			setContentView(R.layout.layout);

			// Create libgdx fragment
			GameFragment libgdxFragment = new GameFragment(this);

			// Put it inside the framelayout (which is defined in the layout.xml file).
			getSupportFragmentManager().beginTransaction().
					add(R.id.content_framelayout, libgdxFragment).
					commit();

			adContainer = findViewById(R.id.ad_container);
			adContainer.setVisibility(View.INVISIBLE);

            Appnext.init(getApplicationContext());
			api = new AppnextAPI(this, "de6b4164-2e42-4f27-a7c3-c86754d9bc9c");
			api.setAdListener(new AppnextAPI.AppnextAdListener() {
				@Override
				public void onError(String error) {
					switch (error){
						case AppnextError.NO_ADS:
							Gdx.app.log("appnext", "no ads");
							findViewById(R.id.ad_container).setVisibility(View.INVISIBLE);
							break;
						case AppnextError.CONNECTION_ERROR:
							Gdx.app.log("appnext", "connection problem");
							findViewById(R.id.ad_container).setVisibility(View.INVISIBLE);
							break;
						default:
							Gdx.app.log("appnext", "other error: "+error);
					}
				}
				@Override
				public void onAdsLoaded(ArrayList<AppnextAd> ads) {
					final AppnextAd firstAd = ads.get(0);
					adTitle = (TextView) findViewById(R.id.title);
					adRating = (TextView) findViewById(R.id.rating);
					adIcon = (ImageView) findViewById(R.id.icon);
					adButton = (Button) findViewById(R.id.install);
					privacyButton = (ImageView) findViewById(R.id.privacy);
					try {
//						Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
						new DownloadImageTask().execute(firstAd.getImageURL());
						adTitle.setText(firstAd.getAdTitle());
						adRating.setText(firstAd.getStoreRating());
						adButton.setText(firstAd.getButtonText());
                        adContainer.setVisibility(View.VISIBLE);
						api.adImpression(firstAd);
					} catch (Exception e) {
						e.printStackTrace();
					}

					adButton.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							api.adClicked(firstAd);
						}
					});

					privacyButton.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							api.privacyClicked(firstAd);
						}
					});
				}
			});
			api.setCreativeType(AppnextAPI.TYPE_STATIC);

			Button clickButton = (Button) findViewById(R.id.closeButton);
			clickButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					adContainer.setVisibility(View.INVISIBLE);
				}
			});
		}

		@Override
		public void exit() {

		}

		@Override
		public void showAds() {
            api.loadAds(new AppnextAdRequest().setCount(1));
		}

		private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

			@Override
			protected Bitmap doInBackground(String[] params) {
				try {
					URL url = new URL(params[0]);
					Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
					return bmp;
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onPostExecute(Bitmap bitmap) {
				adIcon.setImageBitmap(bitmap);
			}
		}
	}


