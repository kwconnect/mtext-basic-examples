package mtext.examples;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import de.kwsoft.mtext.api.activation.ActivationPackage;
import de.kwsoft.mtext.api.activation.ActivationPackageBuilder;
import de.kwsoft.mtext.api.activation.ActivationPackageType;
import de.kwsoft.mtext.api.activation.MTextActivationServer;
import de.kwsoft.mtext.api.activation.MTextFactory;

public class ActivationApiExample {
    private static final Date NOW = new Date();

    // M/TEXT user/password managed in M/User
    private static final String NAME = "master";
    private static final String PASSWORD = "master";

    private static final String RESOURCE_PROJECT = "Test"; // Project of name "Test"
    private static final String RESOURCE_RELATIVE_PATH = ""; // Resource in root directory of project
    private static final String RESOURCE_NAME = "Resource.txt";

    /**
     * Version number of activation package to be built. This version number has to be managed by the user of
     * the Java activation API. If the programmatic activation is used together with the activation feature in
     * M/Workbench, it is important that the version numbers of activation packages for the same project are in
     * ascending order. Version numbers of different projects are independent and don't need to relate to each other.
     */
    private static final String activationNumber = "1.0.0.41";

    public static ActivationPackage createActivationPackage(ActivationPackageBuilder builder) throws Exception {
        // The normal use case is that we access files from a VCS workspace here, but the connection to a VCS is
        // not enforced via the Serie M/ activation API.
        // For the sake of simplicity, this example just uses a ByteArrayInputStream with an test content.
        builder.addResource(RESOURCE_RELATIVE_PATH, RESOURCE_NAME, new ByteArrayInputStream(
            ("Test content, creation date is: " + NOW.toString()).getBytes()));
//        builder.addResourceRemoval(RESOURCE_RELATIVE_PATH, RESOURCE_NAME);
        return builder.finishActivationPackage(NOW);
    }

    public static void sendPackage(ActivationPackage p) throws Exception {
        Properties configuration = new Properties();
        MTextActivationServer as = MTextFactory.connect(NAME, PASSWORD, configuration);
        List<ActivationPackage> packages = new ArrayList<ActivationPackage>();
        packages.add(p);
        as.sendActivationPackages(packages);
    }

    public static void main(String[] args) throws Exception {
        ActivationPackageBuilder builder = MTextFactory.newActivationPackageBuilder(RESOURCE_PROJECT, activationNumber,
            ActivationPackageType.UPDATE);
        ActivationPackage p = createActivationPackage(builder);
        sendPackage(p);
    }
}
