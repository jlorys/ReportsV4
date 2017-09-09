import { Injectable } from '@angular/core';
import {Http, RequestOptions, Headers} from '@angular/http';
import 'rxjs/add/operator/map';
import {Observable} from "rxjs/Observable";
import {PageRequestByExample, PageResponse} from "../../../support/paging";
import {LazyLoadEvent} from "primeng/primeng";
import {Subject} from "./subject";

@Injectable()
export class SubjectDataService {

  private options = new RequestOptions({ headers: new Headers({ 'Content-Type': 'application/json' }) });

  constructor(private http: Http) {}

  /**
   * Delete an Subject by id.
   */
  delete(id: any) {
    return this.http.delete('/api/subject/' + id).catch(this.handleError);
  }

  /**
   * Load a page (for paginated datatable) of Subject using the passed
   * user as an example for the search by example facility.
   */
  getPage(subject: Subject, event: LazyLoadEvent): Observable<PageResponse<Subject>> {
    let req = new PageRequestByExample(subject, event);
    let body = JSON.stringify(req);

    return this.http.post('/api/subject/page', body, this.options)
      .map(response => {
        let pr: any = response.json();
        return new PageResponse<Subject>(pr.totalPages, pr.totalElements, Subject.toArray(pr.content));
      })
      .catch(this.handleError);
  }

  /**
   * Get a Subject by id.
   */
  getSubject(id: any): Observable<Subject> {
    return this.http.get('/api/subject/' + id)
      .map(response => new Subject(response.json()))
      .catch(this.handleError);
  }

  /**
   * Update the passed Subject.
   */
  update(subject: Subject): Observable<Subject> {
    let body = JSON.stringify(subject);

    return this.http.put('/api/subject/', body, this.options)
      .map(response => new Subject(response.json()))
      .catch(this.handleError);
  }

  /**
   * Find all Subjects.
   */
  findAll() : Observable<Subject[]> {
    return this.http.get('/api/subject/findAll/')
      .map(response => Subject.toArray(response.json()))
      .catch(this.handleError);
  }

  // method from angular documentation
  private handleError(error: any) {
    let errMsg = (error.message) ? error.message :
      error.status ? `Status: ${error.status} - Text: ${error.statusText}` : 'Server error';
    console.error(errMsg); // log to console instead
    if (error.status === 401) {
      window.location.href = '/';
    }
    return Observable.throw(errMsg);
  }
}
