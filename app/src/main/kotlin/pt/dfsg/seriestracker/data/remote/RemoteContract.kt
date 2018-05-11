package pt.dfsg.seriestracker.data.remote

class RemoteContract {
    companion object {
        const val ROOT_URl = "http://api.tvmaze.com"

        const val SEARCH = "/search/shows"
        const val SEASONS = "/shows/{showId}/seasons"
        const val EPISODES = "/seasons/{seasonId}/episodes"

    }
}