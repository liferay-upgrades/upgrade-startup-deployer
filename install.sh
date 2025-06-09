install_file_path=".liferay-upgrades-startup-deployer"
file_name="upgrade-startup-deployer.jar"

function getLatestStartupDeployerSnapshot() {
    location="$(curl -I -s https://github.com/liferay-upgrades/upgrade-startup-deployer/releases/latest | grep "location:" | cut -d " " -f 2 | sed 's/.$//')"
        tag="${location##*/}"
        url="https://github.com/liferay-upgrades/upgrade-startup-deployer/releases/download/${tag}/${file_name}"

        echo "Starting download from ${url}"

        #create directory if not exists
        cd ~ && mkdir -p $install_file_path && cd $install_file_path &&
        curl -L \
        -o $file_name \
        $url
}

#$1=functionDeclaration
#$2=functionBody
function writeStartupDeployerFunction {
    echo "function $1 {"$'\n'"    $2"$'\n}'
}

function addAliasOnBashrcFileForStartupDeployer {
    startupDeployerFunctionName="startup_deployer_upgrade_project"

    #only add alias and function if startup_deployer_upgrade_project function is not present on bashrc file
    if ! (grep -q $startupDeployerFunctionName ~/.bashrc); then
        startupFunctionBody="java -jar ~/$install_file_path/$file_name \"\$@\""
        startupFunction=$(writeStartupDeployerFunction "$startupDeployerFunctionName" "$startupFunctionBody")

        aliasFunctionBody="alias sdp=\""$startupDeployerFunctionName"\""
        aliasFunctionName=startup_deployer_upgrade_alias
        aliasFunction=$(writeStartupDeployerFunction "$aliasFunctionName" "$aliasFunctionBody")

        echo $'\n\n'"$startupFunction"$'\n\n'"$aliasFunction"$'\n\n'"$aliasFunctionName">> ~/.bashrc
    fi
}

getLatestStartupDeployerSnapshot
addAliasOnBashrcFileForStartupDeployer