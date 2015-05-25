package ru.varren;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.hibernate.Query;
import org.hibernate.cfg.Configuration;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;


public class Main {
    private static final SessionFactory ourSessionFactory;
    private static final ServiceRegistry serviceRegistry;

    static {
        try {
            Configuration configuration = new Configuration();
            configuration.configure();

            serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();
            ourSessionFactory = configuration.buildSessionFactory(serviceRegistry);
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static Session getSession() throws HibernateException {
        return ourSessionFactory.openSession();
    }

    public static void main(final String[] args) throws Exception {

        createTestData();

        System.out.println("AFTER FIRST INSERTS:");
        logPlaylists();

        Session session = getSession();
        try {
            session.beginTransaction();
            Playlist playlist1 = (Playlist) session.get(Playlist.class, 1);
            Playlist playlist2 = (Playlist) session.get(Playlist.class, 2);
            Playlist playlist3 = (Playlist) session.get(Playlist.class, 3);

            //testing simple swap for 1-st playlist;
            List<Video> videoList = playlist1.getVideos();
            Video tmpVideo = videoList.get(0);
            videoList.set(0,videoList.get(1));
            videoList.set(1,tmpVideo);
            playlist1.setVideos(videoList);
            session.persist(playlist1);

            //testing sort for 2-nd playlist
            Collections.sort(playlist2.getVideos(), new Comparator<Video>() {
                @Override
                public int compare(Video o1, Video o2) {
                    return o1.getVideoId() - o2.getVideoId();
                }
            });
            session.persist(playlist2);
            session.getTransaction().commit();

            //keep ordering for 3-d playlist
        }finally {
            session.flush();
            session.close();
        }

        System.out.println("AFTER CHANGES:");
        System.out.println("12345 playlist now should have  21345 order");
        System.out.println("54321 playlist now should have  12345 order");
        System.out.println("24351 playlist now should stay  24351 order");
        logPlaylists();
    }

    private static void logPlaylists(){
        Session session = getSession();
        try {
            Playlist playlist1 = (Playlist) session.get(Playlist.class, 1);
            Playlist playlist2 = (Playlist) session.get(Playlist.class, 2);
            Playlist playlist3 = (Playlist) session.get(Playlist.class, 3);
            System.out.println(playlist1);
            System.out.println(playlist2);
            System.out.println(playlist3);
        }finally {
            session.close();
        }
    }

    private static void createTestData() {
        final Session session = getSession();
        try {
            session.beginTransaction();
            Playlist playlist1 = generatePlaylist(1, "[12345 Playlist]");
            Playlist playlist2 = generatePlaylist(2, "[54321 Playlist]");
            Playlist playlist3 = generatePlaylist(3, "[24351 Playlist]");

            Video video1 = generateVideo(1);
            Video video2 = generateVideo(2);
            Video video3 = generateVideo(3);
            Video video4 = generateVideo(4);
            Video video5 = generateVideo(5);

            //ordered
            playlist1.getVideos().add(video1);
            playlist1.getVideos().add(video2);
            playlist1.getVideos().add(video3);
            playlist1.getVideos().add(video4);
            playlist1.getVideos().add(video5);

            //reverse order
            playlist2.getVideos().add(video5);
            playlist2.getVideos().add(video4);
            playlist2.getVideos().add(video3);
            playlist2.getVideos().add(video2);
            playlist2.getVideos().add(video1);

            //random order
            playlist3.getVideos().add(video2);
            playlist3.getVideos().add(video4);
            playlist3.getVideos().add(video3);
            playlist3.getVideos().add(video5);
            playlist3.getVideos().add(video1);

            session.persist(playlist1);
            session.persist(playlist2);
            session.persist(playlist3);

            session.getTransaction().commit();
            session.flush();

        } finally {
            session.close();
        }
    }

    private static Video generateVideo(int id) {
        Video video = new Video();
        video.setVideoId(id);
        video.setVideoName("Video " + id);
        return video;
    }

    private static Playlist generatePlaylist(int id, String name) {
        Playlist playlist = new Playlist();
        playlist.setPlaylistId(id);
        playlist.setPlaylistName(name);
        return playlist;
    }
}
