package pt.dfsg.seriestracker.data.remote

object RepositoryProvider {

    fun provideSearchRepository(): Repository {
        return Repository(ApiService.create())
    }
}