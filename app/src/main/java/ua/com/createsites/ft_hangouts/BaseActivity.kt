package ua.com.createsites.ft_hangouts

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.OnLifecycleEvent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.preference.PreferenceManager
import android.os.Bundle

open class BaseActivity: AppCompatActivity() {
	private lateinit var currentTheme: String
	private lateinit var sharedPref: SharedPreferences

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
		currentTheme = sharedPref.getString("theme_chooser", getString(R.string.def_theme))
		when (currentTheme) {
			getString(R.string.def_theme) -> setTheme(R.style.AppTheme)
			getString(R.string.indigo_theme) -> setTheme(R.style.AppTheme_Indigo)
			getString(R.string.dark_theme) -> setTheme(R.style.AppTheme_Dark)
			getString(R.string.pink_theme) -> setTheme(R.style.AppTheme_Pink)
		}
	}

	override fun onResume() {
		super.onResume()
		val selectedTheme = sharedPref.getString("theme_chooser", getString(R.string.def_theme))
		if(currentTheme != selectedTheme)
			recreate()
	}
}