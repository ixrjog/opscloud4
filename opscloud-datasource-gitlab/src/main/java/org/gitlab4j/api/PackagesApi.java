/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2017 Greg Messner <greg@messners.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package org.gitlab4j.api;

import org.gitlab4j.api.models.Package;
import org.gitlab4j.api.models.PackageFile;
import org.gitlab4j.api.models.PackageFilter;

import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Stream;

/**
 * <p>This class implements the client side API for the GitLab Packages API.
 * See <a href="https://docs.gitlab.com/ee/api/packages.html">Packages API at GitLab</a> for more information.</p>
 * 
 * NOTE: This API is not available in the Community edition of GitLab.
 */
public class PackagesApi extends AbstractApi {

    public PackagesApi(GitLabApi gitLabApi) {
        super(gitLabApi);
    }

    /**
     * Get a list of project packages. Both Maven and NPM packages are included in results.
     * When accessed without authentication, only packages of public projects are returned.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/packages</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @return a list of pages in the project's packages
     * @throws GitLabApiException if any exception occurs
     */
    public List<Package> getPackages(Object projectIdOrPath) throws GitLabApiException {
        return (getPackages(projectIdOrPath, getDefaultPerPage()).all());
    }

    /**
     * Get a list of project packages for the specified page. Both Maven and NPM packages are included in results.
     * When accessed without authentication, only packages of public projects are returned.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/packages</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param page the page to get
     * @param perPage the number of Package instances per page
     * @return a list of project packages for the specified range
     * @throws GitLabApiException if any exception occurs
     */
    public List<Package> getPackages(Object projectIdOrPath, int page, int perPage) throws GitLabApiException {
        Response response = get(Response.Status.OK, getPageQueryParams(page, perPage),
                "projects", getProjectIdOrPath(projectIdOrPath), "packages");
        return response.readEntity(new GenericType<List<Package>>() {});
    }

    /**
     * Get a Pager of project packages. Both Maven and NPM packages are included in results.
     * When accessed without authentication, only packages of public projects are returned.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/packages</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param itemsPerPage the number of Package instances per page
     * @return a Pager of project packages for the specified range
     * @throws GitLabApiException if any exception occurs
     */
    public Pager<Package> getPackages(Object projectIdOrPath,  int itemsPerPage) throws GitLabApiException {
        return getPackages(projectIdOrPath,null,itemsPerPage);
    }

    /**
     * Get a Pager of project packages. Both Maven and NPM packages are included in results.
     * When accessed without authentication, only packages of public projects are returned.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/packages</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param filter the PackageFilter instance holding the filter values for the query
     * @param itemsPerPage the number of Package instances per page
     * @return a Pager of project packages for the specified range
     * @throws GitLabApiException if any exception occurs
     */
    public Pager<Package> getPackages(Object projectIdOrPath, PackageFilter filter, int itemsPerPage) throws GitLabApiException {
        MultivaluedMap query = filter!=null?filter.getQueryParams().asMap():null;
        return (new Pager<Package>(this, Package.class, itemsPerPage, query,
            "projects", getProjectIdOrPath(projectIdOrPath), "packages"));
    }

    /**
     * Get a Stream of project packages. Both Maven and NPM packages are included in results.
     * When accessed without authentication, only packages of public projects are returned.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/packages</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @return a Stream of pages in the project's packages
     * @throws GitLabApiException if any exception occurs
     */
    public Stream<Package> getPackagesStream(Object projectIdOrPath) throws GitLabApiException {
        return (getPackages(projectIdOrPath, getDefaultPerPage()).stream());
    }

    /**
     * Get a Stream of project packages. Both Maven and NPM packages are included in results.
     * When accessed without authentication, only packages of public projects are returned.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/packages</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param filter the PackageFilter instance holding the filter values for the query
     * @return a Stream of pages in the project's packages
     * @throws GitLabApiException if any exception occurs
     */
    public Stream<Package> getPackagesStream(Object projectIdOrPath, PackageFilter filter) throws GitLabApiException {
        return (getPackages(projectIdOrPath, filter, getDefaultPerPage()).stream());
    }

    /**
     * Get a single project package.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/packages/:package_id</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param packageId the ID of the package to get
     * @return a Package instance for the specified package ID
     * @throws GitLabApiException if any exception occurs
     */
    public Package getPackage(Object projectIdOrPath, Long packageId) throws GitLabApiException {
        Response response = get(Response.Status.OK, null,
                "projects", getProjectIdOrPath(projectIdOrPath), "packages", packageId);
        return (response.readEntity(Package.class));
    }

    /**
     * Get a list of package files of a single package.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/packages/:package_id/package_files</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param packageId the ID of the package to get the package files for
     * @return a list of PackageFile instances for the specified package ID
     * @throws GitLabApiException if any exception occurs
     */
    public List<PackageFile> getPackageFiles(Object projectIdOrPath, Long packageId) throws GitLabApiException {
        return (getPackageFiles(projectIdOrPath, packageId, getDefaultPerPage()).all());
    }

    /**
     * Get a list of package files of a single package for the specified page.
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/packages/:package_id/package_files</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param packageId the ID of the package to get the package files for
     * @param page the page to get
     * @param perPage the number of PackageFile instances per page
     * @return a list of PackageFile instances for the specified package ID
     * @throws GitLabApiException if any exception occurs
     */
    public List<PackageFile> getPackageFiles(Object projectIdOrPath, Long packageId, int page, int perPage) throws GitLabApiException {
        Response response = get(Response.Status.OK, getPageQueryParams(page, perPage),
                "projects", getProjectIdOrPath(projectIdOrPath), "packages", packageId, "package_files");
        return response.readEntity(new GenericType<List<PackageFile>>() {});
    }

    /**
     * Get a Pager of project package files. 
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/packages/:package_id/package_files</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param packageId the ID of the package to get the package files for
     * @param itemsPerPage the number of PackageFile instances per page
     * @return a Pager of PackageFile instances for the specified package ID
     * @throws GitLabApiException if any exception occurs
     */
    public Pager<PackageFile> getPackageFiles(Object projectIdOrPath,  Long packageId, int itemsPerPage) throws GitLabApiException {
        return (new Pager<PackageFile>(this, PackageFile.class, itemsPerPage, null,
                "projects", getProjectIdOrPath(projectIdOrPath), "packages", packageId, "package_files"));
    }

    /**
     * Get a Stream of project package files. 
     *
     * <pre><code>GitLab Endpoint: GET /projects/:id/packages/:package_id/package_files</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param packageId the ID of the package to get the package files for
     * @return a Stream of PackageFile instances for the specified package ID
     * @throws GitLabApiException if any exception occurs
     */
    public Stream<PackageFile> getPackagesStream(Object projectIdOrPath, Long packageId) throws GitLabApiException {
        return (getPackageFiles(projectIdOrPath, packageId, getDefaultPerPage()).stream());
    }

    /**
     * Deletes a project package.
     *
     * <pre><code>GitLab Endpoint: DELETE /projects/:id/packages/:package_id</code></pre>
     *
     * @param projectIdOrPath the project in the form of an Long(ID), String(path), or Project instance
     * @param packageId the ID of the package to delete
     * @throws GitLabApiException if any exception occurs
     */
    public void deletePackage(Object projectIdOrPath, Long packageId) throws GitLabApiException {

        if (packageId == null) {
            throw new RuntimeException("packageId cannot be null");
        }

        delete(Response.Status.NO_CONTENT, null,"projects", getProjectIdOrPath(projectIdOrPath), "packages", packageId);
    }
}
