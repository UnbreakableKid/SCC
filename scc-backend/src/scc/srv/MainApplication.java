package scc.srv;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import scc.DbResources.CommunityResource;
import scc.DbResources.MediaResource;
import scc.DbResources.PostResource;
import scc.DbResources.UserResource;
import scc.pages.InitialPage;
import scc.pages.ThreadPage;
import scc.search.CognitiveSearch;

import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/")
public class MainApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> set = new HashSet<>();
        set.add(MediaResource.class);
        set.add(UserResource.class);
        set.add(CommunityResource.class);
        set.add(PostResource.class);
        set.add(InitialPage.class);
        set.add(ThreadPage.class);
        set.add(CognitiveSearch.class);
        return set;
    }


}