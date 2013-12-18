package com.noobsqn.echelon.connection;

import com.achimala.leaguelib.connection.LeagueAccount;
import com.achimala.leaguelib.connection.LeagueAccountQueue;
import com.achimala.leaguelib.connection.LeagueConnection;
import com.achimala.leaguelib.connection.LeagueServer;
import com.achimala.leaguelib.errors.LeagueErrorCode;
import com.achimala.leaguelib.errors.LeagueException;
import com.achimala.leaguelib.services.GameService;
import com.achimala.leaguelib.services.LeaguesService;
import com.achimala.leaguelib.services.PlayerStatsService;
import com.achimala.leaguelib.services.SummonerService;
import com.achimala.util.Callback;
import com.gvaneyck.rtmp.TypedObject;
import com.noobsqn.echelon.services.EchGameService;
import com.noobsqn.echelon.services.EchLeaguesService;
import com.noobsqn.echelon.services.EchPlayerStatsService;
import com.noobsqn.echelon.services.EchSummonerService;

/**
 * Created by Samuel on 18/12/13.
 */
public class EchLeagueConnection {
    public LeagueConnection connection;

    private EchSummonerService _summonerService;
    private EchLeaguesService _leaguesService;
    private EchPlayerStatsService _playerStatsService;
    private EchGameService _gameService;
    //private LeagueServer _server;

    //private LeagueAccountQueue _accountQueue;

    public EchLeagueConnection() {
        this(null);
    }

    public EchLeagueConnection(LeagueServer server) {
        connection = new LeagueConnection();
        connection.setServer(server);
    }

    public String toString() {
        return String.format("<LeagueConnection (%d accounts)>", connection.getAccountQueue().getAllAccounts().size());
    }

    //// RTMP

    private LeagueAccount nextValidAccount() throws LeagueException {
        LeagueAccount account = connection.getAccountQueue().nextAccount();
        if(account == null)
            throw new LeagueException(LeagueErrorCode.RTMP_ERROR, toString() + " has no connected account");
        return account;
    }

    /**
     * Performs an API call on the League of Legends RTMP server.
     * Returns the raw packet data from the API call.
     * The API call will go through whichever account the account queue chooses.
     * You should probably not use this method; rather, use one of the services.
     */
    public TypedObject invoke(String service, String method, Object arguments) throws LeagueException {
        return nextValidAccount().invoke(service, method, arguments);
    }

    /**
     * Performs an API call on the League of Legends RTMP server through whichever account the account queue chooses.
     * Same as invoke() but takes place asynchronously on a background thread.
     */
    public void invokeWithCallback(String service, String method, Object arguments, final Callback<TypedObject> callback) {
        try {
            nextValidAccount().invokeWithCallback(service, method, arguments, callback);
        } catch(LeagueException ex) {
            callback.onError(ex);
        }
    }

    //// Services

    /**
     * Represents `summonerService` on the League of Legends RTMP API.
     * This service allows you to interact with Summoners, including retrieving their profile information and other data.
     * In the context of LeagueLib, enables you to get LeagueSummoner objects for summoners you are interested in.
     */
    public EchSummonerService getEchSummonerService() {
        if(_summonerService == null)
            _summonerService = new EchSummonerService(this);
        return _summonerService;
    }

    /**
     * Represents `leaguesServiceProxy` on the League of Legends RTMP API.
     * This service allows you to interact with the new leagues ladder ranking system.
     * You can retrieve league information such as league names, league points, division, tier, rank, etc.
     * In the context of LeagueLib, populates a LeagueSummoner object with leagues information.
     */
    public EchLeaguesService getEchLeaguesService() {
        if(_leaguesService == null)
            _leaguesService = new EchLeaguesService(this);
        return _leaguesService;
    }

    /**
     * Represents `playerStatsService` on the League of Legends RTMP API.
     * This service allows you to interact with player ranked statistics (and some normal game statistics).
     * In the context of LeagueLib, populates a LeagueSummoner object with ranked stats information, etc.
     */
    public EchPlayerStatsService getEchPlayerStatsService() {
        if(_playerStatsService == null)
            _playerStatsService = new EchPlayerStatsService(this);
        return _playerStatsService;
    }

    /**
     * Represents `gameService` on the League of Legends RTMP API.
     * This service allows you to retrieve real-time information about ongoing games.
     * In the context of LeagueLib, populates a LeagueSummoner object with active game data,
     * such as who they're in game with, who they're playing, etc.
     */
    public EchGameService getEchGameService() {
        if(_gameService == null)
            _gameService = new EchGameService(this);
        return _gameService;
    }
}