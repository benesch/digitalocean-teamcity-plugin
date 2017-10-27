<%@ page import="com.cloudcastlegroup.digitaloceanplugin.settings.ProfileConfigurationConstants" %>
<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="apiKeyParamName" value="<%=ProfileConfigurationConstants.API_KEY_PROFILE_SETTING%>"/>
<c:set var="imageSlugParamName" value="<%=ProfileConfigurationConstants.IMAGE_PROFILE_SETTING%>"/>
<c:set var="sshKeyNameParamName" value="<%=ProfileConfigurationConstants.SSH_KEY_PROFILE_SETTING%>"/>
<c:set var="regionIdParamName" value="<%=ProfileConfigurationConstants.REGION_PROFILE_SETTING%>"/>
<c:set var="sizeIdParamName" value="<%=ProfileConfigurationConstants.SIZE_PROFILE_SETTING%>"/>
<c:set var="instancesLimitParamName" value="<%=ProfileConfigurationConstants.INSTANCES_LIMIT_PROFILE_SETTING%>"/>
<c:set var="volumeSizeParamName" value="<%=ProfileConfigurationConstants.VOLUME_SIZE_PROFILE_SETTING%>"/>


<tr>
  <th><label for="${apiKeyParamName}">API Key*:</label></th>
  <td><props:textProperty name="${apiKeyParamName}" className="textProperty longField"/></td>
</tr>

<tr>
  <th><label for="${imageSlugParamName}">Image slug*:</label></th>
  <td>
    <props:textProperty name="${imageSlugParamName}" className="textProperty longField" />
    <span class="smallNote">Use your snapshot slug from Digital Ocean</span>
  </td>
</tr>

<tr>
  <th><label for="${instancesLimitParamName}">Instances limit*:</label></th>
  <td>
    <props:textProperty name="${instancesLimitParamName}" className="textProperty longField" />
    <span class="smallNote">Maximum number of instances that can be started</span>
  </td>
</tr>

<tr>
  <th><label for="${sshKeyNameParamName}">Ssh key name*:</label></th>
  <td>
    <props:textProperty name="${sshKeyNameParamName}" className="textProperty longField" />
    <span class="smallNote">Use your ssh key name from Digital Ocean</span>
  </td>
</tr>

<tr>
  <th><label for="${regionIdParamName}">Region*:</label></th>
  <td>
    <props:selectProperty name="${regionIdParamName}" className="longField">
      <props:option value="ams2"><c:out value="Amsterdam 2"/></props:option>
      <props:option value="ams3"><c:out value="Amsterdam 3"/></props:option>
      <props:option value="blr1"><c:out value="Bangalore 1"/></props:option>
      <props:option value="fra1"><c:out value="Frankfurt 1"/></props:option>
      <props:option value="lon1"><c:out value="London 1"/></props:option>
      <props:option value="nyc1"><c:out value="New York 1"/></props:option>
      <props:option value="nyc2"><c:out value="New York 2"/></props:option>
      <props:option value="nyc3"><c:out value="New York 3"/></props:option>
      <props:option value="sfo1"><c:out value="San Francisco 1"/></props:option>
      <props:option value="sfo1"><c:out value="San Francisco 2"/></props:option>
      <props:option value="sfo2"><c:out value="San Francisco 2"/></props:option>
      <props:option value="sgp1"><c:out value="Singapore 1"/></props:option>
      <props:option value="tor1"><c:out value="Toronto 1"/></props:option>
    </props:selectProperty>
</tr>

<tr>
  <th><label for="${sizeIdParamName}">Size*:</label></th>
  <td>
    <props:selectProperty name="${sizeIdParamName}" className="longField">
      <props:option value="512mb"><c:out value="512 Mb - 1CPU - 20Gb - $5/mo"/></props:option>
      <props:option value="1gb"><c:out value="1 GB - 1CPU - 30Gb - $10/mo"/></props:option>
      <props:option value="2gb"><c:out value="2 GB - 2CPU - 40Gb - $20/mo"/></props:option>
      <props:option value="4gb"><c:out value="4 GB - 2CPU - 60Gb - $40/mo"/></props:option>
      <props:option value="8gb"><c:out value="8 Gb - 4CPU - 80Gb - $80/mo"/></props:option>
      <props:option value="16gb"><c:out value="16 Gb - 8CPU - 160Gb - $160/mo"/></props:option>
      <props:option value="32gb"><c:out value="32 Gb - 12CPU - 320Gb - $320/mo"/></props:option>
      <props:option value="48gb"><c:out value="48 Gb - 16CPU - 480Gb - $480/mo"/></props:option>
      <props:option value="64gb"><c:out value="64 Gb - 20CPU - 640Gb - $640/mo"/></props:option>
      <props:option value="c-2"><c:out value="High perf. CPU: 3 GB - 2CPU - 20Gb - $40/mo"/></props:option>
      <props:option value="c-4"><c:out value="High perf. CPU: 6 GB - 4CPU - 20Gb - $80/mo"/></props:option>
      <props:option value="c-8"><c:out value="High perf. CPU: 12 Gb - 8CPU - 20Gb - $160/mo"/></props:option>
      <props:option value="c-16"><c:out value="High perf. CPU: 24 Gb - 16CPU - 20Gb - $320/mo"/></props:option>
    </props:selectProperty>
</tr>

<tr>
  <th><label for="${volumeSizeParamName}">Volume Size*:</label></th>
  <td>
    <props:textProperty name="${volumeSizeParamName}" className="textProperty longField" />
    <span class="smallNote">The GiB of a volume to attach to agents. If the size is zero, no volume will be used.</span>
  </td>
</tr>
