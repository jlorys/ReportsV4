import {Component} from "@angular/core";
import {FieldOfStudyComponent} from "../../component.admin/fieldofstudy/fieldofstudy.component";

@Component({
  selector: 'reviewerFieldsOfStudies',
  templateUrl: '../../component.admin/fieldofstudy/fieldofstudy.component.html'
})
export class ReviewerFieldOfStudyComponent extends FieldOfStudyComponent {

  onRowSelect(event : any) {
    let id =  event.data.id;
    this.router.navigate(['/reviewerFieldsOfStudies', id]);
  }
}
