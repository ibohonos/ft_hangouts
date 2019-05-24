package ua.com.createsites.ft_hangouts

import android.os.Bundle
import android.preference.PreferenceActivity
import android.os.Build
import android.annotation.TargetApi
import android.preference.PreferenceFragment

class SettingsView: PreferenceActivity()
{
	override fun onCreate(savedInstanceState: Bundle?) {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
			onCreatePreferenceActivity()
		} else {
			onCreatePreferenceFragment()
		}
		super.onCreate(savedInstanceState)
	}

	/**
	 * Wraps legacy [.onCreate] code for Android < 3 (i.e. API lvl
	 * < 11).
	 */
	private fun onCreatePreferenceActivity() {
		addPreferencesFromResource(R.xml.settings)
	}

	/**
	 * Wraps [.onCreate] code for Android >= 3 (i.e. API lvl >=
	 * 11).
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private fun onCreatePreferenceFragment() {
		fragmentManager.beginTransaction()
				.replace(android.R.id.content, MyPreferenceFragment())
				.commit()
	}

	@TargetApi(11)
	class MyPreferenceFragment : PreferenceFragment() {
		override fun onCreate(savedInstanceState: Bundle?) {
			super.onCreate(savedInstanceState)
			addPreferencesFromResource(R.xml.settings) //outer class
			// private members seem to be visible for inner class, and
			// making it static made things so much easier
		}
	}
}