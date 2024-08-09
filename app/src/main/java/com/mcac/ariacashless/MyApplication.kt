package com.mcac.ariacashless

import android.app.AlertDialog
import android.app.Application
import com.mcac.ariacashless.ui.balance.GetBalanceViewModel
import com.mcac.ariacashless.ui.barcode.BarcodeViewModel
import com.mcac.ariacashless.ui.cardvalidate.CardValidateViewModel
import com.mcac.ariacashless.ui.ctoc.CtoCTransferViewModel
import com.mcac.ariacashless.ui.failure.FailureViewModel
import com.mcac.ariacashless.ui.main.MainViewModel
import com.mcac.ariacashless.ui.success.SuccessViewModel
import com.mcac.common.utils.Constants.BASE_URL
import com.mcac.common.utils.EncryptionUtil
import com.mcac.common.utils.TypefaceUtil
import com.mcac.data.di.*
import com.mcac.data.remote.source.RemoteDataSource
import com.mcac.data.remote.source.RemoteDataSourceImpl
import com.mcac.data.repository.Repository
import com.mcac.data.repository.RepositoryImpl
import com.mcac.devices.DeviceManager
import com.mcac.devices.DeviceManagerImpl
import com.mcac.devices.hardwares.IDevice
import com.mcac.devices.ks8123.SZZTKS8123
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class MyApplication: Application() {

    private val appModules = module {
        single<IDevice> { SZZTKS8123(get()) }
        single<DeviceManager> { DeviceManagerImpl(get()) }
        factory  { EncryptionUtil(get()) }
        single<Repository> { RepositoryImpl(get()) }
        single { fileProvider(get()) }
        single { cacheProvider(get()) }
        single { httpLoggingInterceptorProvider() }
        single { okHttpClientProvider(get(),get()) }
        single { retrofitProvider(get(),BASE_URL) }
        single { apiServiceProvider(get()) }
        single<RemoteDataSource> { RemoteDataSourceImpl(get()) }

        viewModel { MainViewModel() }
        viewModel { SuccessViewModel(get()) }
        viewModel { FailureViewModel() }
        viewModel { GetBalanceViewModel(get(),get(),get()) }
        viewModel { CardValidateViewModel(get(),get(),get()) }
        viewModel { BarcodeViewModel(get()) }
        viewModel { CtoCTransferViewModel(get()) }
    }
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@MyApplication)
            modules(appModules)
        }
    com.mcac.common.utils.TypefaceUtil.overrideFont(
        applicationContext,
        "SERIF",
        "fonts/" + "b_yekan.ttf"
    )
    }
}