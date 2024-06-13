package com.therxmv.ershu.di

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import com.therxmv.ershu.ui.home.HomeViewModel
import com.therxmv.ershu.ui.specialtyinfo.SpecialtyInfoViewModel
import com.therxmv.ershu.ui.schedule.ScheduleViewModel
import com.therxmv.ershu.ui.base.BaseViewModel
import com.therxmv.ershu.ui.profile.ProfileViewModel
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
        ScheduleViewModel(get(), get(), get(), get(), get())
    }

    factory {
        SpecialtyInfoViewModel(get(), get())
    }

    factory {
        HomeViewModel(get())
    }

    single {
        BaseViewModel(get())
    }

    single {
        ProfileViewModel(get())
    }
}