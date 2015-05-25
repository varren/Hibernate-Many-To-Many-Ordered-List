package ru.varren;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Video {
    @Id
    private int videoId;
    private String videoName;

    @ManyToMany(cascade = CascadeType.PERSIST)
    private Set<Playlist> playlists = new HashSet<Playlist>();

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public int getVideoId() {
        return videoId;
    }

    public void setVideoId(int videoId) {
        this.videoId = videoId;
    }

    public Set<Playlist> getPlaylists() {
        return playlists;
    }

    public void setPlaylists(Set<Playlist> playlists) {
        this.playlists = playlists;
    }

    @Override
    public String toString() {
        return "["+ getVideoName()+"]";
    }
}
