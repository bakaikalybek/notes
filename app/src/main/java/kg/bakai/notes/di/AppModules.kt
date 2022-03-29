package kg.bakai.notes.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent
import kg.bakai.notes.data.network.NotesService
import kg.bakai.notes.data.repository.CategoryRepositoryImpl
import kg.bakai.notes.data.repository.NotesRepositoryImpl
import kg.bakai.notes.domain.repository.CategoryRepository
import kg.bakai.notes.domain.repository.NotesRepository
import kg.bakai.notes.domain.use_case.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModules {

    private const val BASE_URL = "https://firenotesapi.herokuapp.com/"

    @Provides
    @Singleton
    fun providesHttpLoggingInterceptor() = HttpLoggingInterceptor()
        .apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @Singleton
    @Provides
    fun providesOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient
            .Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .build()

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): NotesService = retrofit.create(NotesService::class.java)

    @Singleton
    @Provides
    fun provideNotesRepository(notesService: NotesService): NotesRepository {
        return NotesRepositoryImpl(api = notesService)
    }

    @Singleton
    @Provides
    fun provideCategoryRepository(notesService: NotesService): CategoryRepository {
        return CategoryRepositoryImpl()
    }

    @Singleton
    @Provides
    fun provideGetAllNotesUseCase(repository: NotesRepository): GetAllNotesUseCase {
        return GetAllNotesUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideCreateNoteUseCase(repository: NotesRepository): CreateNoteUseCase {
        return CreateNoteUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideGetNoteUseCase(repository: NotesRepository): GetNoteUseCase {
        return GetNoteUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideDeleteNoteUseCase(repository: NotesRepository): DeleteNoteUseCase {
        return DeleteNoteUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideUpdateNoteUseCase(repository: NotesRepository): UpdateNoteUseCase {
        return UpdateNoteUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideGetAllCategoriesUseCase(repository: CategoryRepository): GetAllCategoriesUseCase {
        return GetAllCategoriesUseCase(repository)
    }

}