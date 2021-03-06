/**
 * The MIT License (MIT)
 * Copyright (c) 2012 David Carver
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
 * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF
 * OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package us.nineworlds.serenity.ui.listeners;

import us.nineworlds.serenity.SerenityApplication;
import us.nineworlds.serenity.core.SerenityConstants;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.core.services.WatchedVideoAsyncTask;
import us.nineworlds.serenity.ui.util.VideoPlayerIntentUtils;
import us.nineworlds.serenity.ui.video.player.SerenitySurfaceViewVideoActivity;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Common class used by both the Poster Gallery view for itemClicks and the
 * Grid View.  It launches video play back when a poster is selected.
 * 
 * @author dcarver
 *
 */
public class AbstractVideoOnItemClickListener {
	
	private SharedPreferences prefs;
	protected VideoContentInfo videoInfo;

	/**
	 * @param v
	 */
	protected void onItemClick(View v) {
		
		if (!SerenityApplication.getVideoPlaybackQueue().isEmpty()) {
			Toast.makeText(v.getContext(), "Cleared video queue before playback.", Toast.LENGTH_LONG).show();
			SerenityApplication.getVideoPlaybackQueue().clear();
		}
		ImageView mpiv = (ImageView) v;
	
		prefs = PreferenceManager
				.getDefaultSharedPreferences(v.getContext());
		boolean externalPlayer = prefs.getBoolean("external_player", false);
	
		if (externalPlayer) {
			Activity activity = (Activity) v.getContext();
			VideoPlayerIntentUtils.launchExternalPlayer(videoInfo, activity);
			//new WatchedVideoAsyncTask().execute(videoInfo.id());
			return;
		}
	
		launchInternalPlayer(mpiv);
	}

	/**
	 * @param mpiv
	 * @return
	 */
	protected void launchInternalPlayer(ImageView view) {
		
		SerenityApplication.getVideoPlaybackQueue().add(videoInfo);
		Activity a = (Activity) view.getContext();
	
		Intent vpIntent = new Intent(a,
				SerenitySurfaceViewVideoActivity.class);
		
		a.startActivityForResult(vpIntent, SerenityConstants.BROWSER_RESULT_CODE);
	}

}
