/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ve.gob.minpal.serviciobi.ws;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.ws.BindingType;
import ve.gob.minpal.serviciobi.impl.CargaInicialImpl;

/**
 * Interfaz que expone el servicio de carga inicial que ejecuta un Kettle Job de
 * Pentaho Data Integration
 *
 * @author Avelardo Gavide
 * @version Versión incial
 *
 */
/**
 *
 * @editado por: simonts
 *
 * 20/05/2016 Se agregaron nuevos parametros de entrada para ejecutar el job Se
 * optimizo y adapto al nvo job que ejecuta todo Se eliminaron parametros
 * sobrantes
 *
 */
@WebService(name = "CargaInicial", targetNamespace = "http://ws.serviciobi.minpal.gob.ve/",
        portName = "CargaInicialPort", serviceName = "CargaInicial")
@BindingType(javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class CargaInicial {

    private final CargaInicialImpl cargaInicialImpl = new CargaInicialImpl();

    /**
     * Método que ejecuta un comando de sistema para lanzar un Kettle Job de
     * Pentaho Data Integration
     *
     *
     * @param	dirPDI	-Directorio donde esta el pdi en el host
     * @param	nombreJob	-Nombre del job a ejecutar
     * @param	dirEjecucion	-Directorio del repositorio donde se encuentra el job
     * a ejecutar
     * @param	repositorio	-Nombree del repositorio
     * @param	usuarioRepositorio	-Nombre del usuario del repositorio
     * @param	passUsuarioRepo	-Password del usuario del repositorio
     *
     * @param	hostBDOracle	-Ip o nombre del host de la bd en Oracle
     * @param	usuarioBDOracle	-Nombre del usuario de la bd en Oracle
     * @param	passUsuarioBDOracle	-Password del usuario de la bd en oracle
     * @param	bdOracle	-Nombre de la base de datos en Oracle
     * @param	hostBDCassandra	-Host de la BD Cassandra
     * @param	colummFamily	-Nombre del column family en Cassandra
     * @param	keySpace	-Nombre del keyspace en Cassandra
     *
     * @param	hostBDApp	-Ip o nombre del host de la bd de la aplicacion
     * @param	usuarioBDApp	-Nombre del usuario de la bd de la aplicacion
     * @param	passUsuarioBDApp	-Password del usuario de la bd de la aplicacion
     * @param	bdApp	-Nombre de la base de datos de la aplicacion
     * @param	idPlanEjec	-Identificador del plan de ejecucion
     * @param	jobModo	-Modo de ejecucion del Job: CARGA o CONTROL
     *
     * @param	dirLogs	-Nombre del directorio para almacenar logs
     * @param	nivelLogs	-Nivel de los logs a ejecutar: Basic, Detailed, Debug,
     * Rowlevel, Error, Nothing
     *
     *
     * @return
     * @throws ExcepcionServicio
     *
     *
     */

    @WebMethod(operationName = "cargainicial", action = "urn:Cargainicial")
    public int cargainicial(
            @WebParam(name = "dirPDI") @XmlElement(name = "dirPDI", required = true, nillable = false) String dirPDI,
            @WebParam(name = "nombreJob") @XmlElement(name = "nombreJob", required = true, nillable = false) String nombreJob,
            @WebParam(name = "dirEjecucion") @XmlElement(name = "dirEjecucion", required = true, nillable = false) String dirEjecucion,
            @WebParam(name = "repositorio") @XmlElement(name = "repositorio", required = true, nillable = false) String repositorio,
            @WebParam(name = "usuarioRepositorio") @XmlElement(name = "usuarioRepositorio", required = true, nillable = false) String usuarioRepositorio,
            @WebParam(name = "passUsuarioRepo") @XmlElement(name = "passUsuarioRepo", required = true, nillable = false) String passUsuarioRepo,
            @WebParam(name = "hostBDOracle") @XmlElement(name = "hostBDOracle", required = true, nillable = false) String hostBDOracle,
            @WebParam(name = "usuarioBDOracle") @XmlElement(name = "usuarioBDOracle", required = true, nillable = false) String usuarioBDOracle,
            @WebParam(name = "passUsuarioBDOracle") @XmlElement(name = "passUsuarioBDOracle", required = true, nillable = false) String passUsuarioBDOracle,
            @WebParam(name = "bdOracle") @XmlElement(name = "bdOracle", required = true, nillable = false) String bdOracle,
            @WebParam(name = "hostBDCassandra") @XmlElement(name = "hostBDCassandra", required = true, nillable = false) String hostBDCassandra,
            @WebParam(name = "colummFamily") @XmlElement(name = "colummFamily", required = true, nillable = false) String colummFamily,
            @WebParam(name = "keySpace") @XmlElement(name = "keySpace", required = true, nillable = false) String keySpace,
            @WebParam(name = "hostBDApp") @XmlElement(name = "hostBDApp", required = true, nillable = false) String hostBDApp,
            @WebParam(name = "usuarioBDApp") @XmlElement(name = "usuarioBDApp", required = true, nillable = false) String usuarioBDApp,
            @WebParam(name = "passUsuarioBDApp") @XmlElement(name = "passUsuarioBDApp", required = true, nillable = false) String passUsuarioBDApp,
            @WebParam(name = "bdApp") @XmlElement(name = "bdApp", required = true, nillable = false) String bdApp,
            @WebParam(name = "idPlanEjec") @XmlElement(name = "idPlanEjec", required = true, nillable = false) String idPlanEjec,
            @WebParam(name = "jobModo") @XmlElement(name = "jobModo", required = true, nillable = false) String jobModo,
            @WebParam(name = "dirLogs") @XmlElement(name = "dirLogs", required = true, nillable = false) String dirLogs,
            @WebParam(name = "nivelLogs") @XmlElement(name = "nivelLogs", required = true, nillable = false) String nivelLogs
    ) {
        return cargaInicialImpl.cargainicial(dirPDI, nombreJob, dirEjecucion, repositorio, usuarioRepositorio, passUsuarioRepo, hostBDOracle, usuarioBDOracle, passUsuarioBDOracle, bdOracle, hostBDCassandra, colummFamily, keySpace, hostBDApp, usuarioBDApp, passUsuarioBDApp, bdApp, idPlanEjec, jobModo, dirLogs, nivelLogs);
    }
}
