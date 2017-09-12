import {Component} from "@angular/core";
import {LaboratoryAddComponent} from "../../component.admin/laboratory/laboratory.add.component";

@Component({
  selector: 'reviewerLaboratories-add',
  templateUrl: '../../component.admin/laboratory/laboratory.add.component.html',
})
export class ReviewerLaboratoryAddComponent extends LaboratoryAddComponent {

  onRowSelect(event : any) {
    let id =  event.data.id;
    this.router.navigate(['/reviewerReports', id]);
  }
}
