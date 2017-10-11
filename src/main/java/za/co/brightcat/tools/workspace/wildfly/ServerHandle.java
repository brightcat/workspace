/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.co.brightcat.tools.workspace.wildfly;

/**
 *
 * @author G984716
 */
public interface ServerHandle {

    void addJmsQueues(JmsQueue... queues);
    
}
