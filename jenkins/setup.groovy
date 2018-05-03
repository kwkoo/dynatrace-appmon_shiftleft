#!groovy

import jenkins.model.Jenkins
import jenkins.model.GlobalConfiguration
import hudson.model.*
import hudson.tools.JDKInstaller
import hudson.tools.InstallSourceProperty
import hudson.security.*
import jenkins.security.s2m.AdminWhitelistRule

// create admin user
//
println 'create admin user'
def instance = Jenkins.getInstance()
def hudsonRealm = new HudsonPrivateSecurityRealm(false)
hudsonRealm.createAccount("admin", "admin")
instance.setSecurityRealm(hudsonRealm)
def strategy = new FullControlOnceLoggedInAuthorizationStrategy()
instance.setAuthorizationStrategy(strategy)
instance.save()
Jenkins.instance.getInjector().getInstance(AdminWhitelistRule.class).setMasterKillSwitch(false)

// set JDK
//
println 'add JDK8'
Jenkins.instance.updateCenter.getById('default').updateDirectlyNow(true)
def jdk = new JDK("JDK8", "/usr/lib/jvm/java-8-oracle")
def descriptor = new JDK.DescriptorImpl()
descriptor.setInstallations(jdk)


// install Maven 3
//
println 'add M3'
mavenPlugin = Jenkins.instance.getExtensionList(hudson.tasks.Maven.DescriptorImpl.class)[0]
newMavenInstall = new hudson.tasks.Maven.MavenInstallation('M3', null, [new hudson.tools.InstallSourceProperty([new hudson.tasks.Maven.MavenInstaller("3.5.0")])])
mavenPlugin.installations += newMavenInstall
mavenPlugin.save()

// configure Dynatrace plugin
//
def dtplugin = Descriptor.findById(GlobalConfiguration.all(), "com.dynatrace.jenkins.dashboard.TAGlobalConfiguration")
if (dtplugin != null) {
  println "Configuring Dynatrace plugin"
  dtplugin.protocol = "http"
  dtplugin.host = "dtserver"
  dtplugin.port = 8020
  dtplugin.save()
}
