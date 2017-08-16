import {Component} from '@angular/core';
import {HashLocationStrategy, LocationStrategy} from "@angular/common";

@Component({
  selector: 'page-not-found',
  template: '<p>Page Not Found</p>',
  providers: [Location, {provide: LocationStrategy, useClass: HashLocationStrategy}]
})
export class PageNotFoundComponent {

}
