package ru.varren;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
@Entity
public class Playlist {
    @Id
    private int playlistId;
    private String playlistName;
    @ManyToMany(cascade = CascadeType.PERSIST)
    @OrderColumn
    private List<Video> videos = new ArrayList<Video>();

    public int getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(int playlistId) {
        this.playlistId = playlistId;
    }

    public String getPlaylistName() {
        return playlistName;
    }

    public void setPlaylistName(String playlistName) {
        this.playlistName = playlistName;
    }

    public List<Video> getVideos() {
        return videos;
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }

    @Override
    public String toString() {
        String strVal = getPlaylistName() +" #"+ getPlaylistId() +": ";
        for(int i = 0; i<videos.size(); i++){
            strVal+= "\n"+ i+": "+videos.get(i);
        }
        return strVal;
    }
}
