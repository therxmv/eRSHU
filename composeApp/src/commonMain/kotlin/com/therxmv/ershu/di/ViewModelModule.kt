package com.therxmv.ershu.di

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import com.therxmv.ershu.ui.base.BaseViewModel
import com.therxmv.ershu.ui.home.viewmodel.HomeViewModel
import com.therxmv.ershu.ui.profile.viewmodel.ProfileViewModel
import com.therxmv.ershu.ui.rating.viewmodel.RatingViewModel
import com.therxmv.ershu.ui.schedule.viewmodel.ScheduleViewModel
import com.therxmv.ershu.ui.specialtyinfo.viewmodel.SpecialtyInfoViewModel
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import org.koin.dsl.module
import org.koin.mp.KoinPlatform.getKoin

@Composable
inline fun <reified T : ScreenModel> Screen.getScreenModel(
    qualifier: Qualifier? = null,
    noinline parameters: ParametersDefinition? = null
): T {
    val koin = getKoin()
    return rememberScreenModel(tag = qualifier?.value) { koin.get(qualifier, parameters) }
}

val viewModelModule = module {
    single { // TODO maybe make single and implement refresh
        ScheduleViewModel(get(), get(), get(), get(), get(), get())
    }

    factory {
        SpecialtyInfoViewModel(get(), get(), get())
    }

    factory {
        HomeViewModel(get(), get())
    }

    single {
        BaseViewModel(get(), get())
    }

    single {
        ProfileViewModel(get())
    }

    single {
        RatingViewModel(get(), get())
    }
}