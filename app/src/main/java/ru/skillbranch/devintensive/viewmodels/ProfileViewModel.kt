package ru.skillbranch.devintensive.viewmodels

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.skillbranch.devintensive.models.Profile
import ru.skillbranch.devintensive.repositories.PreferencesRepository

class ProfileViewModel: ViewModel() {
    private val repository: PreferencesRepository = PreferencesRepository
    private val profileData = MutableLiveData<Profile>()
    private val appTheme = MutableLiveData<Int>()
    private val isValidationError = MutableLiveData<Boolean>()

    init {
        profileData.value = repository.getProfile()
        appTheme.value = repository.getAppTheme()
    }

    fun getProfileData(): LiveData<Profile> = profileData

    fun getTheme(): LiveData<Int> = appTheme

    fun getIsValidationError(): LiveData<Boolean> = isValidationError

    fun saveProfileData(profile: Profile) {
        repository.saveProfile(profile)
        profileData.value = profile
    }

    fun switchTheme() {
        if (appTheme.value == AppCompatDelegate.MODE_NIGHT_YES) {
            appTheme.value = AppCompatDelegate.MODE_NIGHT_NO
        } else {
            appTheme.value = AppCompatDelegate.MODE_NIGHT_YES
        }

        repository.saveAppTheme(appTheme.value!!)
    }

    fun checkRepository(repositoryText: String) {
        isValidationError.value = repositoryIsValidate(repositoryText).not()
    }

    private fun repositoryIsValidate(repositoryText: String): Boolean {
        val excludedParts = listOf(
                "enterprise", "features", "topics", "collections", "trending",
                "events", "marketplace", "pricing", "nonprofit", "customer-stories",
                "security", "login", "join"
        ).joinToString("|")

        val regex = Regex(
                "^(https://)?(www\\.)?(github\\.com/)(?!($excludedParts)(?=/|\$))[\\w\\d](?:[\\w\\d]|-(?=[\\w\\d])){0,39}(/)?$"
        )

        return repositoryText.contains(regex)
    }
}