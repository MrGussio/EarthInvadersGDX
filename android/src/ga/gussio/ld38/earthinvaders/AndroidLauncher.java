	package ga.gussio.ld38.earthinvaders;

	import android.graphics.Bitmap;
	import android.graphics.BitmapFactory;
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
	import com.badlogic.gdx.backends.android.AndroidApplication;
	import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
	import com.badlogic.gdx.backends.android.AndroidFragmentApplication;

	import java.io.IOException;
	import java.net.MalformedURLException;
	import java.net.URL;
	import java.util.ArrayList;

	public class AndroidLauncher extends FragmentActivity implements AndroidFragmentApplication.Callbacks {

		private AppnextAPI api;

		@Override
		public void onCreate (Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);

			setContentView(R.layout.layout);

			// Create libgdx fragment
			GameFragment libgdxFragment = new GameFragment();

			// Put it inside the framelayout (which is defined in the layout.xml file).
			getSupportFragmentManager().beginTransaction().
					add(R.id.content_framelayout, libgdxFragment).
					commit();

			findViewById(R.id.ad_container).setVisibility(View.VISIBLE);

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
					AppnextAd firstAd = ads.get(0);
					TextView adTitle = (TextView) findViewById(R.id.adTitle);
					TextView adDescription = (TextView) findViewById(R.id.adDescription);
					ImageView adIcon = (ImageView) findViewById(R.id.adIcon);
					Button adButton = (Button) findViewById(R.id.installButton);

					try {
						URL url = new URL(firstAd.getImageURL());
						Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
						adTitle.setText(firstAd.getAdTitle());
						adDescription.setText(firstAd.getAdDescription());
						adIcon.setImageBitmap(bmp);
						adButton.setText(firstAd.getButtonText());
						findViewById(R.id.ad_container).setVisibility(View.VISIBLE);
					} catch (MalformedURLException e) {
						e.printStackTrace();
					} catch (IOException e){
						e.printStackTrace();
					}

				}
			});
			api.setCreativeType(AppnextAPI.TYPE_STATIC);
			api.loadAds(new AppnextAdRequest().setCount(1));

			Button clickButton = (Button) findViewById(R.id.closeButton);
			clickButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					findViewById(R.id.ad_container).setVisibility(View.INVISIBLE);
				}
			});
		}

		@Override
		public void exit() {

		}
	}
